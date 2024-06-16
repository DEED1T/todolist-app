package m2sdl.prjdevops.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseMetricsAspect {

    private final MeterRegistry meterRegistry;

    public DatabaseMetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(* org.springframework.jdbc.core.JdbcTemplate.*(..))")
    public Object measureDatabaseCall(ProceedingJoinPoint pjp) throws Throwable {
        Timer timer = meterRegistry.timer("database.response.time");
        return timer.recordCallable(() -> {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}
