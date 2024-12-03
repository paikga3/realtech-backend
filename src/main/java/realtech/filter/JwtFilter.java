package realtech.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String token = extractJwtFromCookies(request);

        try {
            if (requestURI.startsWith("/api/admin")) {
                if (token == null) {
                    throw new UnauthorizedException("Unauthorized: No token provided");
                }
            }
            
            validateAndAuthenticateUser(token);

            // 다음 필터 또는 컨트롤러로 요청 전달
            filterChain.doFilter(request, response);

        } catch (UnauthorizedException ex) {
            handleUnauthorizedException(response, ex);
        } finally {
            // SecurityContext를 항상 클리어
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.clearContext();
            }
        }
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("auth".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    private void validateAndAuthenticateUser(String token) {
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

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, UnauthorizedException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + ex.getMessage() + "\"}");
    }
}
