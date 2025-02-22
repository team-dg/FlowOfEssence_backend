package com.lolclone.chatdomain.repository.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.lolclone.chatdomain.domain.MemberStatus;
import com.lolclone.chatdomain.domain.friend.Friend;
import com.lolclone.chatdomain.domain.friend.FriendshipStatus;
import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.FriendSortCondition;
import com.lolclone.chatdomain.repository.friend.query.QChatRoomInfoDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendRelationDto;
import com.lolclone.chatdomain.repository.friend.query.QFriendStateDto;
import com.lolclone.commonmodule.utils.QuerydslRepositorySupport;
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

import jakarta.persistence.EntityManager;

import static com.lolclone.chatdomain.domain.friend.QFriend.friend1;
import static com.lolclone.chatdomain.domain.message.QMessage.message;
import static com.lolclone.chatdomain.domain.chatroom.QChatRoom.chatRoom;
import static com.lolclone.chatdomain.domain.chatparticipant.QChatParticipant.chatParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FriendRepositoryImpl extends QuerydslRepositorySupport implements QuerydslFriendRepository {
    public FriendRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        super(Friend.class, queryFactory, entityManager);
    }

    @Override
    public JPAQuery<Long> countFriends(UUID userId) {
        return fetchCount(query -> query
            .select(friend1.count())
            .from(friend1)
            .where(
                friend1.user.id.eq(userId),
                friend1.friendshipStatus.status.eq(FriendshipStatus.Status.ACTIVE)
            )
        );
    }

    @Override
    public Page<FriendChatInfoDto> findFriends(UUID userId, FriendSortCondition sortCondition, Pageable pageable) {
        return applyPagination(pageable,
            query -> {
                JPAQuery<FriendChatInfoDto> baseQuery = createBaseFriendQuery(userId);

                // 정렬 조건 적용
                if (sortCondition.hasAnySortCondition()) {
                    baseQuery.orderBy(createOrderSpecifiers(sortCondition));
                }

                return baseQuery;
            },
            query -> countFriends(userId)
        );
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(FriendSortCondition sortCondition) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        
        // 상태 기준 정렬
        if (sortCondition.sortByStatus()) {
          orders.add(new OrderSpecifier<>(Order.DESC,
            new CaseBuilder()
              .when(friend1.friend.stateInfo.status.eq(MemberStatus.ONLINE)).then(3)
              .when(friend1.friend.stateInfo.status.eq(MemberStatus.AWAY)).then(2)
              .when(friend1.friend.stateInfo.status.eq(MemberStatus.IN_GAME)).then(1)
              .otherwise(0)
          ));
        }
        
        // 닉네임 기준 정렬
        if (sortCondition.sortByNickname()) {
          orders.add(new OrderSpecifier<>(Order.ASC, friend1.friend.nickname));
        }
        
        return orders.toArray(new OrderSpecifier[0]);
      }

    private JPAQuery<FriendChatInfoDto> createBaseFriendQuery(UUID userId) {
        return select(new QFriendChatInfoDto(
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
            select(message.count().intValue())
                .from(message)
                .where(message.chatRoom.eq(chatRoom)
                .and(message.createdDate.gt(chatParticipant.lastReadMessage.readAt))),
            "unreadMessageCount"
        );
    }
}
