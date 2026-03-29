package com.substring.auth.auth_security_app_backend.Security;

import com.substring.auth.auth_security_app_backend.repositories.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1) Fetch the Authorization Header which holds token as-> Bearer sjgjskgkfg
        String header = request.getHeader("Authorization");
        logger.info("Authorization header :{}",header);
        if (header != null && header.startsWith("Bearer ")) {
            //2) Extract the token
            String token=header.substring(7);


            //3)Do exception handling
            try{
                //check for access token
                if(!jwtService.isAccessToken(token)){
                    filterChain.doFilter(request,response);
                    return;
                }
                //4) Parse the token
                Jws<Claims> parse= jwtService.parse(token);
                //5) Fetch the Claims and subject(userid)

                Claims payload=parse.getPayload();

                String userId = payload.getSubject();

                UUID userUuid = UUID.fromString(userId);

                //Got the user from the Database
                userRepository.findById(userUuid).ifPresent(user -> {
                    //Check for user Enable or not
                    if(user.isEnable()) {
                        List<GrantedAuthority> authorities = user.
                                getRoles() == null ? List.of()
                                : user.getRoles().stream().
                                map(role -> new SimpleGrantedAuthority(role.getName())).
                                collect(Collectors.toUnmodifiableList());


                        //Now Create the authentication token

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        //Final Line to set the authentication to SecurityContextHolder
                        if (SecurityContextHolder.getContext().getAuthentication() == null)
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    });

            }catch(ExpiredJwtException e){
                request.setAttribute("error", "token expired");
                //e.printStackTrace();

            } catch(JwtException e){
                request.setAttribute("error", "token invalid");
                //e.printStackTrace();

            } catch(Exception e){
                request.setAttribute("error", "token invalid");
                //e.printStackTrace();

            }
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/v1/auth");
    }
}
