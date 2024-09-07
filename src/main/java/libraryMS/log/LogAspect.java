package libraryMS.log;

import jakarta.servlet.http.HttpServletRequest;
import libraryMS.dao.LogDao;
import libraryMS.domain.ApplicationUser;
import libraryMS.domain.LogDomain;
import libraryMS.security.service.ApplicationUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    @Autowired
    LogDao logDao;

    Map<String, String> map = new HashMap<>();
    Map<String, String> mapOldValue = new HashMap<>();

    @AfterReturning(value = ("@annotation(libraryMS.log.Log)"), returning = "returnValue")
    public void log(JoinPoint joinPoint, Object returnValue) throws Throwable {
        Object[] args = joinPoint.getArgs();

        ApplicationUser applicationUser = ApplicationUserDetails.getCurrentUser();

        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Parameter[] parameter = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        int i = 0;

        LogDomain logDomain = new LogDomain();
        for (Object arg : args) {
            Object value = ((MethodSignature) joinPoint.getSignature()).getReturnType();
            if (arg instanceof Loggable) {
                logDomain.setEntityId(((Loggable) returnValue).fetchId());
                logDomain.setEntityName((((Loggable) arg).getRelatedEntity()));
                for (Field f : returnValue.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.get(returnValue) != null) {
                        map.put(f.getName(), f.get(returnValue).toString());
                    }
                }
                logDomain.setNewValue(map.toString());
                logDomain.setApplicationUser(applicationUser);
                logDomain.setOldValue(mapOldValue.toString());
            }
            i++;
        }
        logDao.save(logDomain);
    }

}
