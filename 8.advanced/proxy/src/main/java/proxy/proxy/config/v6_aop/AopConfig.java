package proxy.proxy.config.v6_aop;

import proxy.proxy.config.AppV1Config;
import proxy.proxy.config.AppV2Config;
import proxy.proxy.config.v6_aop.aspect.LogTraceAspect;
import proxy.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
