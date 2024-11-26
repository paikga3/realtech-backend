package realtech.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

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

        // 현재 실행 중인 메서드의 정보 가져오기
        Method method = getTargetMethod(joinPoint);

        // 매개변수 이름 기반으로 refTable과 refId를 추출
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        String refTable = null;
        Integer refId = null;

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestParam) {
                    RequestParam requestParam = (RequestParam) annotation;
                    String paramName = requestParam.value();

                    if ("refTable".equals(paramName) && args[i] instanceof String) {
                        refTable = (String) args[i];
                    } else if ("refId".equals(paramName) && args[i] instanceof Integer) {
                        refId = (Integer) args[i];
                    }
                }
            }
        }

        // refTable 또는 refId가 없으면 예외 처리
        if (refTable == null || refId == null) {
            throw new RuntimeException("Missing required parameters: refTable or refId");
        }

        // 토큰 검증
        JwtValidator.validateToken(token, refTable, refId);
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
