package com.lolclone.chatinfra.service.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lolclone.chatdomain.domain.gameinvite.GameInvite;
import com.lolclone.chatdomain.domain.gameinvite.GameInviteStatus;
import com.lolclone.chatdomain.domain.member.Member;
import com.lolclone.chatdomain.repository.GameInviteRepository;
import com.lolclone.chatdomain.repository.MemberRepository;
import com.lolclone.chatinfra.exception.commonexception.BadRequestException;
import com.lolclone.chatinfra.exception.commonexception.NotFoundException;
import com.lolclone.chatinfra.exception.domain.ExceptionType;
import com.lolclone.chatserviceapi.dto.GameInviteDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameInviteService {
    private final GameInviteRepository gameInviteRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    // /**
    //  * 게임 초대 생성
    //  */
    // public GameInviteDto.Response createGameInvite(GameInviteDto.Request request) {
    //     Member inviter = findMemberById(MemberId.of(request.getInviterId()));
    //     Member invitee = findMemberById(MemberId.of(request.getInviteeId()));

    //     validateGameInviteCreation(inviter, invitee);

    //     GameInvite gameInvite = GameInvite.create(inviter, invitee);
    //     GameInvite savedInvite = gameInviteRepository.save(gameInvite);

    //     // 게임 초대 알림 생성
    //     notificationService.createGameInviteNotification(savedInvite);

    //     return GameInviteDto.Response.from(savedInvite);
    // }

    // public GameInviteDto.Response acceptGameInvite(GameInviteId inviteId) {
    //     GameInvite gameInvite = findGameInviteById(inviteId);
    //     gameInvite.accept();

    //     // 게임 초대 수락 알림 생성
    //     notificationService.createGameInviteAcceptedNotification(gameInvite);

    //     return GameInviteDto.Response.from(gameInviteRepository.save(gameInvite));
    // }

    // public GameInviteDto.Response rejectGameInvite(GameInviteId inviteId) {
    //     GameInvite gameInvite = findGameInviteById(inviteId);
    //     gameInvite.reject();

    //     // 게임 초대 거절 알림 생성
    //     notificationService.createGameInviteRejectedNotification(gameInvite);

    //     return GameInviteDto.Response.from(gameInviteRepository.save(gameInvite));
    // }

    // /**
    //  * 게임 초대 취소
    //  */
    // public GameInviteDto.Response cancelGameInvite(GameInviteId inviteId) {
    //     GameInvite gameInvite = findGameInviteById(inviteId);
    //     gameInvite.cancel();

    //     // 게임 초대 취소 알림 생성
    //     notificationService.createGameInviteCanceledNotification(gameInvite);

    //     return GameInviteDto.Response.from(gameInviteRepository.save(gameInvite));
    // }

    // @Scheduled(fixedRate = 60000) // 1분마다 실행
    // public void expireOldInvites() {
    //     List<GameInvite> pendingInvites = gameInviteRepository.findPendingInvites().stream()
    //         .filter(invite -> invite.getMetadata().getExpiresAt().isBefore(LocalDateTime.now()))
    //         .collect(Collectors.toList());

    //     pendingInvites.forEach(invite -> {
    //         invite.expire();
    //         notificationService.createGameInviteExpiredNotification(invite);
    //     });

    //     gameInviteRepository.saveAll(pendingInvites);
    // }

    // /**
    //  * 사용자의 받은 게임 초대 목록 조회
    //  */
    // @Transactional(readOnly = true)
    // public List<GameInviteDto.Response> getReceivedGameInvites(MemberId userId) {
    //     Member member = findMemberById(userId);

    //     return gameInviteRepository.findByInviteeAndStatus(member, GameInviteStatus.pending())
    //         .stream()
    //         .map(GameInviteDto.Response::from)
    //         .collect(Collectors.toList());
    // }

    // private void validateGameInviteCreation(Member inviter, Member invitee) {
    //     if (gameInviteRepository.existsPendingInvite(inviter, invitee)) {
    //         throw new BadRequestException(ExceptionType.GAME_INVITE_ALREADY_SENT);
    //     }
    // }

    // private Member findMemberById(MemberId memberId) {
    //     return memberRepository.findById(memberId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    // }

    // private GameInvite findGameInviteById(GameInviteId inviteId) {
    //     return gameInviteRepository.findById(inviteId)
    //             .orElseThrow(() -> new NotFoundException(ExceptionType.GAME_INVITE_NOT_FOUND));
    // }
}
