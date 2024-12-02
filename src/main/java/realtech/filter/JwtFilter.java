package realtech.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import realtech.api.common.exception.UnauthorizedException;
import realtech.db.entity.Account;
import realtech.db.repository.AccountRepository;
import realtech.security.AuthenticatedUser;
import realtech.util.JwtValidator;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = null;

        // 쿠키에서 JWT 추출
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("auth".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // /admin/** 요청에만 JWT 검증 수행
        if (requestURI.startsWith("/api/admin")) {
            if (token == null) {
                throw new UnauthorizedException("Unauthorized: No token provided");
            }

            JwtValidator.validateToken(token);
        } else {
            if (token != null) {
                Claims claims = JwtValidator.validateToken(token);
                
                int id = Integer.parseInt(claims.get("id").toString());
                
                Optional<Account> accountOpt = accountRepository.findById(id);
                if (accountOpt.isPresent()) {
                    Account account = accountOpt.get();
                    
                    AuthenticatedUser userDetails = new AuthenticatedUser(
                            account.getUsername(),
                            account.getName(),
                            account.getEmail(),
                            account.getPhoneNumber()
                            );
                    // Spring Security 인증 객체 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // SecurityContext에 인증 정보 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}