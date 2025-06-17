package io.twogether.nbe_5_7_2_02team.member.domain;

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity;

import jakarta.persistence.*;

import lombok.*;
import lombok.AccessLevel;

@Entity
public class Follow extends BaseEntity {

    public Follow() {}

    public Long getId() {
        return id;
    }

    public Member getFollower() {
        return follower;
    }

    public Member getFollowing() {
        return following;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    public Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    public Member following;

    public Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }
}
