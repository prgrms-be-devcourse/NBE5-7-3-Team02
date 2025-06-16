package io.twogether.nbe_5_7_2_02team.member.domain;

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity;

import jakarta.persistence.*;

import lombok.*;

import org.springframework.util.StringUtils;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String email;

    @Setter public String name;

    public String profileImage;

    @Column(nullable = false)
    public String githubId;

    @Setter public String job;

    @Setter public String course;

    @Enumerated(EnumType.STRING)
    public Role role;

    public Member(Role role, String email, String profileImage, String githubId) {
        this.role = role;
        this.email = email;
        this.profileImage = profileImage;
        this.githubId = githubId;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        if (StringUtils.hasText(nickname)) {
            this.name = nickname;
        }
        if (StringUtils.hasText(profileImageUrl)) {
            this.profileImage = profileImageUrl;
        }
    }

    @Builder
    public Member(
            String email,
            String name,
            String profileImage,
            String job,
            String course,
            String githubId,
            Role role) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.job = job;
        this.course = course;
        this.githubId = githubId;
        this.role = role;
    }
}
