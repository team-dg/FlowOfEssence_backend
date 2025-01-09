package com.lolclone.chatinfra.service.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lolclone.chatdomain.domain.ChatParticipant;
import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatdomain.repository.ChatParticipantRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ChatParticipantServiceTest {
    @Mock
    private ChatParticipantRepository chatParticipantRepository;
    
    @Mock
    private MessageService messageService;
    
    @InjectMocks
    private ChatParticipantService chatParticipantService;
    
    @BeforeEach
    void setUp() {
        
    }

    @Test
    public void test_get_or_throw_returns_participant_when_exists() {
        // Given
        Long participantId = 1L;
        ChatParticipant expectedParticipant = mock(ChatParticipant.class);
        when(chatParticipantRepository.findById(participantId)).thenReturn(Optional.of(expectedParticipant));

        // When
        ChatParticipant result = chatParticipantService.getOrThrow(participantId);

        // Then
        assertThat(result).isEqualTo(expectedParticipant);
        verify(chatParticipantRepository).findById(participantId);
    }

    @Test
    public void test_has_unread_messages_returns_correct_status() {
        // Given
        Long participantId = 1L;
        Long latestMessageId = 100L;
        ChatParticipant participant = mock(ChatParticipant.class);
        when(chatParticipantRepository.findById(participantId)).thenReturn(Optional.of(participant));
        when(participant.hasUnreadMessages(latestMessageId)).thenReturn(true);

        // When
        boolean result = chatParticipantService.hasUnreadMessages(participantId, latestMessageId);

        // Then
        assertThat(result).isTrue();
        verify(participant).hasUnreadMessages(latestMessageId);
    }

    @Test
    public void test_create_chat_participant_with_valid_chat_room_and_user() {
        // Given
        ChatRoom chatRoom = mock(ChatRoom.class);
        Member user = mock(Member.class);
        ChatParticipant expectedParticipant = ChatParticipant.of(chatRoom, user);
        when(chatParticipantRepository.save(any(ChatParticipant.class))).thenReturn(expectedParticipant);

        // When
        ChatParticipant result = chatParticipantService.createChatParticipant(chatRoom, user);

        // Then
        assertThat(result).isEqualTo(expectedParticipant);
        verify(chatParticipantRepository).save(any(ChatParticipant.class));
    }

    @Test
    public void test_update_last_read_message_for_valid_participant() {
        // Given
        Long participantId = 1L;
        Long messageId = 100L;
        ChatParticipant participant = mock(ChatParticipant.class);
        when(chatParticipantRepository.findById(participantId)).thenReturn(Optional.of(participant));

        // When
        chatParticipantService.updateLastReadMessage(participantId, messageId);

        // Then
        verify(participant).updateLastReadMessage(messageId);
        verify(chatParticipantRepository).findById(participantId);
    }
}
