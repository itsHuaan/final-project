package org.example.final_project.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String JWT_SECRET;
    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;

    //private final IUserService userService;
    private final IUserRepository userRepository;

//    public String generateTokenByUsername(String username) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//        //UserDto userDto = userService.findByUsername(username);
//        UserEntity user = userRepository.findByUsername(username).get();
//        return Jwts.builder()
//                .setSubject(Long.toString(user.getUserId()))
//                .claim("username", user.getUsername())
//                .claim("email", user.getEmail())
//                .claim("role", user.getRole().getRoleId())
//                .setExpiration(expiryDate)
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//                .compact();
//    }

    public String generateTokenByEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
       UserEntity userEntity = userRepository.findByEmail(email).get();
        return Jwts.builder()
                .setSubject(Long.toString(userEntity.getUserId()))
                .claim("username", userEntity.getUsername())
                .claim("email", userEntity.getEmail())
                .claim("role", userEntity.getRole().getRoleId())
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateForgetPasswordToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        UserEntity userEntity = userRepository.findByEmail(email).get();
        return Jwts.builder()
                .setSubject(email)
                .claim("username", userEntity.getUsername())
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getKeyByValueFromJWT(String key, String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get(key, String.class);
    }

    public Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
