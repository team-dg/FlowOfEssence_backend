package com.lolclone.chatdomain.repository.friend;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lolclone.chatdomain.repository.friend.query.FriendChatInfoDto;

public interface QuerydslFriendRepository {
    // 전체 친구 수 조회
    long countFriends(UUID userId);
    // 닉네임 기준 친구 목록 조회
    Page<FriendChatInfoDto> findFriendByNickname(UUID userId, boolean sortByNickname, Pageable pageable);
    // 온라인 상태 기준 친구 목록 조회
    Page<FriendChatInfoDto> findFriendsByStatus(UUID userId, boolean sortByStatus, Pageable pageable);
}
