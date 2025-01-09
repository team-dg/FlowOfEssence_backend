package com.lolclone.chatinfra.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lolclone.chatdomain.domain.ChatRoom;
import com.lolclone.chatdomain.domain.ChatRoomType;
import com.lolclone.chatdomain.domain.Member;
import com.lolclone.chatinfra.service.domain.ChatRoomService;
import com.lolclone.chatinfra.service.domain.FriendService;
import com.lolclone.chatinfra.service.domain.MemberService;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private MemberService memberService;
    
    @Mock
    private FriendService friendService;
    
    @Mock
    private ChatRoomService chatRoomService;
    
    @InjectMocks
    private ChatService chatService;

    private UUID userId;
    private UUID friendId;
    private Member user;
    private Member friend;
    private ChatRoom expectedRoom;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        friendId = UUID.randomUUID();
        
        // Member 엔티티 생성 (도메인 객체)
        user = Member.builder()
            .id(userId)
            .nickname("testUser")
            .build();
            
        friend = Member.builder()
            .id(friendId)
            .nickname("testFriend")
            .build();
            
        // ChatRoom 엔티티 생성 (도메인 객체)
        expectedRoom = ChatRoom.builder()
            .type(ChatRoomType.PERSONAL)
            .build();
    }

    @Test
    public void test_get_existing_personal_chat_room() {
        Map<UUID, Member> members = Map.of(userId, user, friendId, friend);
    
        when(memberService.getMembersByIds(Arrays.asList(userId, friendId))).thenReturn(members);
        doNothing().when(friendService).validateFriendship(user, friend);
        when(chatRoomService.getPersonalRoom(user, friend)).thenReturn(expectedRoom);
    
        // Act
        ChatRoom result = chatService.getOrCreatePersonalChatRoom(userId, friendId);
    
        // Assert
        assertNotNull(result);
        assertEquals(expectedRoom, result);
        verify(memberService).getMembersByIds(Arrays.asList(userId, friendId));
        verify(friendService).validateFriendship(user, friend);
        verify(chatRoomService).getPersonalRoom(user, friend);
    }
}
