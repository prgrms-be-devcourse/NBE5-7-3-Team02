package io.twogether.nbe_5_7_2_02team.chat.domain;

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity;
import io.twogether.nbe_5_7_2_02team.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ChatMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChatMemberStatus chatMemberStatus;

    public ChatMember(ChatRoom chatRoom, Member member, ChatMemberStatus chatMemberStatus) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.chatMemberStatus = chatMemberStatus;
    }

    protected ChatMember() {
    }

    public Long getId() {
        return id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatMemberStatus(ChatMemberStatus chatMemberStatus) {
        this.chatMemberStatus = chatMemberStatus;
    }

    public Member getMember() {
        return member;
    }

    public ChatMemberStatus getChatMemberStatus() {
        return chatMemberStatus;
    }
}
