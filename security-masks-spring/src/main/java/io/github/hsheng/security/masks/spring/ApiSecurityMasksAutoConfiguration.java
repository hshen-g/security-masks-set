package io.github.hsheng.security.masks.spring;


import io.github.hsheng.security.masks.MaskHandler;
import io.github.hsheng.security.masks.config.MaskConfig;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * API掩码配置类
 * @author Staunch
 * @version 1.0
 * @date 2025/1/1
 * @since 1.0
 */
@Configuration
public class ApiSecurityMasksAutoConfiguration implements ResourceLoaderAware, EnvironmentAware {
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();
    /**
     * resource 资源加载器
     */
    private ResourceLoader resourceLoader;
    private Environment env;
     private MaskConfig maskConfigProperties() throws IOException {
        String location = env.getProperty("security-masks.location");
        Resource resource = resourceLoader.getResource(location);
        return YAML_MAPPER.readValue(resource.getFile(), MaskConfig.class);
    }
    @Bean
    public MaskHandler handler() throws IOException {
        return new MaskHandler(maskConfigProperties());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
