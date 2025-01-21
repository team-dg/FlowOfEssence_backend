package com.lolclone.chatdomain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lolclone.chatdomain.domain.friendrequest.FriendRequest;
import com.lolclone.chatdomain.domain.friendrequest.FriendRequestId;
import com.lolclone.chatdomain.domain.member.Member;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {
    @Query("SELECT fr FROM FriendRequest fr " +
           "WHERE fr.receiver = :receiver " +
           "AND fr.status.status = 'PENDING' " +
           "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findPendingRequestsByReceiver(@Param("receiver") Member receiver);
    
    @Query("SELECT COUNT(fr) > 0 FROM FriendRequest fr " +
            "WHERE fr.requester = :requester " +
            "AND fr.receiver = :receiver " +
            "AND fr.status.status = 'PENDING'")
    boolean existsPendingRequest(@Param("requester") Member requester, @Param("receiver") Member receiver);
}

