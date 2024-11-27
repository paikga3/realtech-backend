package realtech.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import realtech.api.front.model.BaseCommentRequest;
import realtech.util.AesEncryptionUtil;
import realtech.util.JwtValidator;

@Aspect
@Component
public class JwtValidationAspect {
    private final HttpServletRequest httpServletRequest;

    public JwtValidationAspect(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Before("execution(* realtech.api.front.controller.CommentController.*(..))")
    public void validateToken(JoinPoint joinPoint) throws Exception {
        // Authorization 헤더 처리
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = AesEncryptionUtil.decode(authorizationHeader.substring(7));

        // 공통 클래스 기반 파라미터 처리
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseCommentRequest) {
                BaseCommentRequest baseRequest = (BaseCommentRequest) arg;
                JwtValidator.validateToken(token, baseRequest.getRefTable(), baseRequest.getRefId());
                return;
            }
        }
    }

    private Method getTargetMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();

        for (Method method : targetClass.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                return method;
            }
        }
        throw new NoSuchMethodException("Method not found: " + methodName);
    }
}
