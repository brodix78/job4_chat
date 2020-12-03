package ru.job4j.chat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.job4j.chat.model.Person;

import com.auth0.jwt.JWT;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    protected static final String SECRET = "MyUltraSecretKey";
    protected static final long EXPIRATION_DATE = 3600 * 24 * 10;
    protected static final String TOKEN_PREFIX = "Bearer";
    protected static final String HEADER_STRING = "Authorization";
    protected static final String SIGN_UP_URL = "user/signup";

    private AuthenticationManager auth;

    public AuthenticationFilter(AuthenticationManager auth) {
        this.auth = auth;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            Person user = new ObjectMapper().readValue(request.getInputStream(), Person.class);
            return auth.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getLogin(), user.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth) {
        String token = JWT.create().withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

}
