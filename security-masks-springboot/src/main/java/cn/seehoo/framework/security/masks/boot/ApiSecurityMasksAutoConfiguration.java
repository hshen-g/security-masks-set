package cn.seehoo.framework.security.masks.boot;


import cn.seehoo.framework.security.masks.MaskHandler;
import cn.seehoo.framework.security.masks.config.MaskConfig;
import org.aspectj.lang.JoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * API掩码配置类
 * @author Staunch
 * @version 1.0
 * @date 2025/1/1
 * @since 1.0
 */
@Configuration
public class ApiSecurityMasksAutoConfiguration {
    private final String MASK_CONFIG_PREFIX = "security-masks";
    @Bean
    @ConfigurationProperties(prefix = MASK_CONFIG_PREFIX)
    public MaskConfig maskConfigProperties(){
        return new MaskConfig();
    }
    @Bean
    public MaskHandler handler(MaskConfig maskConfig){
        return new MaskHandler(maskConfig);
    }
    @Bean
    @ConditionalOnClass({JoinPoint.class, DispatcherServlet.class})
    @ConditionalOnProperty(prefix = MASK_CONFIG_PREFIX, name = "rest-aop", havingValue = "true")
    public RestControllerMaskAspect maskAspect(MaskHandler handler){
        return new RestControllerMaskAspect(handler);
    }
}
