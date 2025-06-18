package io.twogether.nbe_5_7_2_02team.member.dto.response;

import io.twogether.nbe_5_7_2_02team.member.domain.Member;
import io.twogether.nbe_5_7_2_02team.member.domain.Role;

public class SignUpResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String job;
    private final String course;
    private final Role role;

    SignUpResponse(Long id, String email, String name, String job, String course, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.job = job;
        this.course = course;
        this.role = role;
    }

    public static SignUpResponse from(Member member) {
        return SignUpResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .job(member.getJob())
                .course(member.getCourse())
                .role(member.getRole())
                .build();
    }

    public static SignUpResponseBuilder builder() {
        return new SignUpResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getJob() {
        return this.job;
    }

    public String getCourse() {
        return this.course;
    }

    public Role getRole() {
        return this.role;
    }

    public static class SignUpResponseBuilder {

        private Long id;
        private String email;
        private String name;
        private String job;
        private String course;
        private Role role;

        SignUpResponseBuilder() {}

        public SignUpResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SignUpResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SignUpResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SignUpResponseBuilder job(String job) {
            this.job = job;
            return this;
        }

        public SignUpResponseBuilder course(String course) {
            this.course = course;
            return this;
        }

        public SignUpResponseBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public SignUpResponse build() {
            return new SignUpResponse(
                    this.id, this.email, this.name, this.job, this.course, this.role);
        }

        public String toString() {
            return "SignUpResponse.SignUpResponseBuilder(id="
                    + this.id
                    + ", email="
                    + this.email
                    + ", name="
                    + this.name
                    + ", job="
                    + this.job
                    + ", course="
                    + this.course
                    + ", role="
                    + this.role
                    + ")";
        }
    }
}
