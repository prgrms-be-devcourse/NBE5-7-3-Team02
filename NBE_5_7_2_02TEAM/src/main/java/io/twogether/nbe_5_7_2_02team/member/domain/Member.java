package io.twogether.nbe_5_7_2_02team.member.domain;

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity;

import jakarta.persistence.*;

import lombok.*;

import org.springframework.util.StringUtils;

import java.util.*;

@Entity
public class Member extends BaseEntity {

    protected Member() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getGithubId() {
        return githubId;
    }

    public String getJob() {
        return job;
    }

    public String getCourse() {
        return course;
    }

    public Role getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String name;

    private String profileImage;

    @Column(nullable = false)
    private String githubId;

    private String job;

    private String course;

    @Enumerated(EnumType.STRING)
    private Role role;

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
