package org.example.final_project.configuration.Oauth2;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2UserCustom implements OAuth2User {
    private String id;
    private String name;
    private String email;
    private String accessToken;
    private Instant expiresAt;
    private String thumbnail;
    private List<GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}
