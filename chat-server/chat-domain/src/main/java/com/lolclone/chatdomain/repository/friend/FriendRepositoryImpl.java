package com.lolclone.chatdomain.repository.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.friend.FriendshipStatus;
import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.QChatRoomInfoDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendRelationDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendStateDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.lolclone.chatdomain.domain.friend.QFriend.friend1;
import static com.lolclone.chatdomain.domain.message.QMessage.message;
import static com.lolclone.chatdomain.domain.chatroom.QChatRoom.chatRoom;
import static com.lolclone.chatdomain.domain.chatparticipant.QChatParticipant.chatParticipant;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements QuerydslFriendRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public long countFriends(UUID userId) {
        return queryFactory
                .select(friend1.count())
                .from(friend1)
                .where(
                    friend1.user.id.eq(userId),
                    friend1.friendshipStatus.status.eq(FriendshipStatus.Status.ACTIVE))
                .fetchOne();
    }

    @Override
    public Page<FriendChatInfoDto> findFriendByNickname(UUID userId, boolean sortByNickname, Pageable pageable) {
        JPAQuery<FriendChatInfoDto> query = createBaseFriendQuery(userId);

        if (sortByNickname) {
            query.orderBy(friend1.friend.nickname.asc());
        } 

        List<FriendChatInfoDto> content = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, countFriends(userId));
    }

    @Override
    public Page<FriendChatInfoDto> findFriendsByStatus(UUID userId, boolean sortByStatus, Pageable pageable) {
        JPAQuery<FriendChatInfoDto> query = createBaseFriendQuery(userId);

        if (sortByStatus) {
            query.orderBy(new OrderSpecifier<>(Order.DESC,
                new CaseBuilder()
                    .when(friend1.friend.stateInfo.status.eq(MemberStatus.ONLINE)).then(3)
                    .when(friend1.friend.stateInfo.status.eq(MemberStatus.AWAY)).then(2)
                    .when(friend1.friend.stateInfo.status.eq(MemberStatus.IN_GAME)).then(1)
                    .otherwise(0)
            ));
        }

        List<FriendChatInfoDto> content = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, countFriends(userId));
    }

    private JPAQuery<FriendChatInfoDto> createBaseFriendQuery(UUID userId) {
        return queryFactory
            .select(new QFriendChatInfoDto(
                friend1.friend.id,
                friend1.friend.nickname,
                new QFriendStateDto(
                    friend1.friend.stateInfo.status,
                    createGameInfoExpression(),
                    friend1.friend.updatedDate,
                    friend1.friend.stateInfo.status.ne(MemberStatus.OFFLINE)
                ),
                new QChatRoomInfoDto(
                    chatRoom.id,
                    message.messageContent.content,
                    message.sender.id,
                    message.createdDate,
                    chatParticipant.status.muted,
                    createUnreadMessageCountSubQuery()
                ),
                new QFriendRelationDto(
                    friend1.memo,
                    friend1.friend.tags,
                    friend1.friendshipStatus.status.eq(FriendshipStatus.Status.BLOCKED)
                )
            ))
            .from(friend1)
            .leftJoin(chatRoom).on(chatRoom.id.in(
                    JPAExpressions
                            .select(chatRoom.id)
                            .from(chatRoom)
                            .join(chatParticipant).on(chatParticipant.chatRoom.eq(chatRoom))
                            .where(chatParticipant.user.id.eq(userId))))
            .leftJoin(chatParticipant).on(chatParticipant.chatRoom.eq(chatRoom)
                .and(chatParticipant.user.id.eq(userId)))
            .leftJoin(message).on(message.eq(
                JPAExpressions
                        .select(message)
                        .from(message)
                        .where(message.chatRoom.eq(chatRoom))
                        .orderBy(message.createdDate.desc())
                        .limit(1)))
            .where(
                friend1.user.id.eq(userId),
                friend1.friendshipStatus.status.eq(FriendshipStatus.Status.ACTIVE)
            );
    }

    private StringExpression createGameInfoExpression() {
        return new CaseBuilder()
            .when(friend1.friend.gameInfo.isNotNull())
            .then(Expressions.stringTemplate(
                "CONCAT({0}, ' ', {1}, '게임 중')",
                friend1.friend.gameInfo.gameMode.stringValue(),
                friend1.friend.gameInfo.gameType.stringValue()
            ))
            .otherwise((String)null);
    }

    private Expression<Integer> createUnreadMessageCountSubQuery() {
        return ExpressionUtils.as(
            JPAExpressions
                .select(message.count().intValue())
                .from(message)
                .where(message.chatRoom.eq(chatRoom)
                .and(message.createdDate.gt(chatParticipant.lastReadMessage.readAt))),
            "unreadMessageCount"
        );
    }
}
