package com.lolclone.chatdomain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lolclone.chatdomain.domain.gameinvite.GameInvite;
import com.lolclone.chatdomain.domain.gameinvite.GameInviteStatus;
import com.lolclone.chatdomain.domain.member.Member;

public interface GameInviteRepository extends JpaRepository<GameInvite, UUID> {
    //@Query("SELECT gi FROM GameInvite gi WHERE gi.status.status = 'PENDING'")
    //List<GameInvite> findPendingInvites();

    //@Query("SELECT COUNT(gi) > 0 FROM GameInvite gi " +
    //        "WHERE gi.inviter = :inviter " +
    //        "AND gi.invitee = :invitee " +
    //        "AND gi.status.status = 'PENDING'")
    //boolean existsPendingInvite(@Param("inviter") Member inviter, @Param("invitee") Member invitee);

    //@Query("SELECT gi FROM GameInvite gi WHERE gi.invitee = :invitee AND gi.status = :status")
    //List<GameInvite> findByInviteeAndStatus(@Param("invitee") Member invitee, @Param("status") GameInviteStatus status);
}
