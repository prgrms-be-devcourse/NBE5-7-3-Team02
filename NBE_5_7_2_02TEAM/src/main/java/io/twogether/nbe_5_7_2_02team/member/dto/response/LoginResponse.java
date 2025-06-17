package io.twogether.nbe_5_7_2_02team.member.dto.response;

import io.twogether.nbe_5_7_2_02team.member.domain.Role;
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {
    public TokenPair tokenPair;

    public LoginResponse(TokenPair tokenPair, Role role, Long memberId) {
        this.tokenPair = tokenPair;
        this.role = role;
        this.memberId = memberId;
    }

    public Role role;
    public Long memberId;
}
