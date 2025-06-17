package io.twogether.nbe_5_7_2_02team.chat.service;

import static io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode.CHAT_ROOM_ALREADY_EXISTS;
import static io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode.NOT_FOUND_POST;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatRoomRepository;
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom;
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse;
import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset;
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException;
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository;
import io.twogether.nbe_5_7_2_02team.member.domain.Member;
import io.twogether.nbe_5_7_2_02team.member.domain.Role;
import io.twogether.nbe_5_7_2_02team.post.dao.PostRepository;
import io.twogether.nbe_5_7_2_02team.post.domain.Post;
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@FlywayReset
@Transactional
@SpringBootTest
class ChatRoomServiceTest {

    @Autowired private ChatRoomService chatRoomService;
    @Autowired private ChatRoomRepository chatRoomRepository;

    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;

    private Post post;
    private Long chatRoomId;

    @BeforeEach
    void setUp() {
        chatRoomRepository.deleteAll();
        postRepository.deleteAll();

        Member member =
                new Member(
                        "email@test.com", "name", "image", "job", "course", "gitid", Role.MEMBER);
        memberRepository.save(member);

        post = new Post("제목", "내용", RecruitmentStatus.NONE, member);

        postRepository.save(post);

        chatRoomId = chatRoomService.createChatroom(post.getId());
    }

    @Test
    @DisplayName("채팅방 생성 테스트: 성공")
    void createChatRoomTest() {
        Optional<ChatRoom> byId = chatRoomRepository.findById(chatRoomId);

        ChatRoom chatRoom = byId.get();

        System.out.println("========================================");
        System.out.println("POST: " + post.getId());
        System.out.println("========================================");
        System.out.println("ChatRoom ID: " + chatRoom.getId());
        System.out.println("ChatRoom POST: " + chatRoom.getPost().getId());
        System.out.println("createdAt: " + chatRoom.getCreatedAt());
        System.out.println("updatedAt: " + chatRoom.getUpdatedAt());
        System.out.println("========================================");
    }

    @Test
    @DisplayName("채팅방 생성 테스트: 에러 - 없는 게시글 생성 테스트")
    void createChatRoomNotFoundPostTest() {
        ErrorException errorException =
                assertThrows(ErrorException.class, () -> chatRoomService.createChatroom(1000L));

        assertEquals(NOT_FOUND_POST, errorException.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 생성 테스트: 에러 - 중복생성 테스트")
    void createChatRoomDuplicateTest() {
        ErrorException errorException =
                assertThrows(
                        ErrorException.class, () -> chatRoomService.createChatroom(post.getId()));

        assertEquals(CHAT_ROOM_ALREADY_EXISTS, errorException.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 목록 조회 테스트: 성공")
    void getChatRoomListTest() {
        List<ChatRoomGetResponse> chatRoomList = chatRoomService.getChatRoomList();

        System.out.println("========================================");
        for (ChatRoomGetResponse chatRoom : chatRoomList) {
            System.out.println("ID: " + chatRoom.getId());
        }
        System.out.println("========================================");
    }
}
