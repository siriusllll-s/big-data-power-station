package qrsoft.information.shared.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysLogAspect {

	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint point, SysLog sysLog) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return point.proceed();
		} finally {
			System.out.println("[SysLog] " + sysLog.action() + " cost=" + (System.currentTimeMillis() - start) + "ms");
		}
	}
}
