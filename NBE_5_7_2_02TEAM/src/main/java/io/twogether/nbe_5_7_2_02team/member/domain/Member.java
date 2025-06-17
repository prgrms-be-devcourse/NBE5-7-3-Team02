package io.twogether.nbe_5_7_2_02team.member.domain;

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.springframework.util.StringUtils;

@Entity
public class Member extends BaseEntity {

    protected Member() {}

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

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

    public static class MemberBuilder {

        private String email;
        private String name;
        private String profileImage;
        private String job;
        private String course;
        private String githubId;
        private Role role;

        MemberBuilder() {}

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public MemberBuilder job(String job) {
            this.job = job;
            return this;
        }

        public MemberBuilder course(String course) {
            this.course = course;
            return this;
        }

        public MemberBuilder githubId(String githubId) {
            this.githubId = githubId;
            return this;
        }

        public MemberBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(
                    this.email,
                    this.name,
                    this.profileImage,
                    this.job,
                    this.course,
                    this.githubId,
                    this.role);
        }

        public String toString() {
            return "Member.MemberBuilder(email="
                    + this.email
                    + ", name="
                    + this.name
                    + ", profileImage="
                    + this.profileImage
                    + ", job="
                    + this.job
                    + ", course="
                    + this.course
                    + ", githubId="
                    + this.githubId
                    + ", role="
                    + this.role
                    + ")";
        }
    }
}
