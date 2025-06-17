package io.twogether.nbe_5_7_2_02team.oauth.dto.common;

import io.twogether.nbe_5_7_2_02team.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenBody {
    public Long memberId;
    public Role role;

    public TokenBody(Long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }
}
