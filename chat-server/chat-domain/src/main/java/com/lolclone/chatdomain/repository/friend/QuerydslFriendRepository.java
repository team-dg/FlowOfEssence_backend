package com.lolclone.chatdomain.repository.friend;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;
import com.lolclone.chatdomain.repository.friend.query.FriendSortCondition;
import com.querydsl.jpa.impl.JPAQuery;

public interface QuerydslFriendRepository {
    // 전체 친구 수 조회
    JPAQuery<Long> countFriends(UUID userId);
    // 친구 목록 조회 (닉네임 또는 상태 정렬 지원)
    Page<FriendChatInfoDto> findFriends(UUID userId, FriendSortCondition sortCondition, Pageable pageable);
}
