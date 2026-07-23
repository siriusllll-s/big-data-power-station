package qrsoft.information.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class SysLogAspect {
	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint pjp, SysLog sysLog) throws Throwable {
		System.out.println("[SysLog] " + sysLog.action());
		return pjp.proceed();
	}
}
