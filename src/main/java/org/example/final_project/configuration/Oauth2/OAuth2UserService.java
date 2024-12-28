package org.example.final_project.configuration.Oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.final_project.configuration.jwt.JwtProvider;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.util.Const;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.example.final_project.util.RandomMethods.generateUsername;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final IUserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    //chạy hàm này đầu tiên
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //load thông tin từ phương thức đăng nhập
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provideName = null;
        OAuth2UserCustom userCustom = OAuth2UserCustom.builder().build();
        if (userRequest.getClientRegistration().getClientName().equals("google")) {
            provideName = Const.GOOGLE;
            userCustom = getUserPropertiesForGoogle(oAuth2User);
        } else if (userRequest.getClientRegistration().getClientName().equals("facebook")) {
            provideName = Const.FACEBOOK;
            userCustom = getUserPropertiesForFaceBook(oAuth2User);
        }

        userCustom.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_BUYER"));
        userCustom.setAccessToken(userRequest.getAccessToken().getTokenValue());
        userCustom.setExpiresAt(userRequest.getAccessToken().getExpiresAt());
        UserEntity user = userRepository.findByEmail(userCustom.getEmail()).orElse(null);
        if (user == null) {
            user = UserEntity.builder()
                    .email(userCustom.getEmail())
                    .role(new RoleEntity(2L, userCustom.getAuthorities().toString()))
                    .shop_status(0)
                    .provider(provideName)
                    .name(userCustom.getName())
                    .profilePicture(userCustom.getThumbnail())
                    .username(generateUsername(userCustom.getEmail()))
                    .createdAt(LocalDateTime.now())
                    .isActive(1)
                    .gender(-1)
                    .build();
        } else {
            user.setDeletedAt(null);
            user.setIsActive(1);
        }
        userRepository.save(user);
        return oAuth2User;
    }

    public AuthenticationFailureHandler onFailureHandler() {
        return ((request, response, authentication) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            final Map<String, Object> body = new HashMap<>();
            body.put("timestamp", new Date());
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Bad credentials Oauth2");
            body.put("message", "Invalid username or password");
            body.put("path", request.getRequestURI());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        });
    }

    public AuthenticationSuccessHandler onSuccessHandler() {
        return ((request, response, authentication) -> {
            OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
            String providerName = oauth2Authentication.getAuthorizedClientRegistrationId();
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = null;
            if (providerName.equals("google")) {
                email = defaultOAuth2User.getAttributes().get("email").toString();
            }
            if (providerName.equals("facebook")) {
                email = defaultOAuth2User.getAttributes().get("email").toString();
            }
            UserEntity user = userRepository.findByEmail(email).orElse(null);
            String jwt = jwtProvider.generateTokenByEmail(user.getEmail());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            String redirectUrl = String.format(
                    "https://team03.cyvietnam.id.vn/en/login/success?token=%s&userId=%s&userName=%s&email=%s",
                    jwt,
                    user.getUserId(),
                    URLEncoder.encode(user.getName(), StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8.toString())
            );
            response.sendRedirect(redirectUrl);
        });
    }

    private OAuth2UserCustom getUserPropertiesForGoogle(OAuth2User oAuth2User) {
        Assert.notNull(oAuth2User.getAttribute("sub"), "google's id");
        Assert.notNull(oAuth2User.getAttribute("name"), "google's name");
        Assert.notNull(oAuth2User.getAttribute("email"), "google's email");
        Assert.notNull(oAuth2User.getAttribute("picture"), "google's picture");
        return OAuth2UserCustom
                .builder()
                .id(oAuth2User.getAttribute("sub").toString())
                .name(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .thumbnail(oAuth2User.getAttribute("picture"))
                .build();
    }

    private OAuth2UserCustom getUserPropertiesForFaceBook(OAuth2User oAuth2User) {

        Assert.notNull(oAuth2User.getAttribute("name"), "facebook's name");
        Assert.notNull(oAuth2User.getAttribute("email"), "facebook's email");

        return OAuth2UserCustom.builder()
                .name(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .build();
    }
}
