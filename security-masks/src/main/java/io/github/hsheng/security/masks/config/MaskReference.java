package io.github.hsheng.security.masks.config;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 掩码引用配置：即什么场景（一般是API）使用什么掩码配置
 * 优先级: disabled>specified>global
 * @author yuanx
 * @version 1.0
 * @date 2025/8/5 17:55
 * @since 1.0
 */
public class MaskReference {
    public static final String FIELD_NO_EXISTS = "NaN";
    /**
     * 全局缺省配置：常用字段掩码映射关系，适配任何场景
     */
    @Getter
    private final Map<String,String> global = new HashMap<>();
    /**
     * 禁用的场景配置：在这里配置的场景（支持通配符，Ant风格），不做掩码处理，优先级比specified的高
     */
    @Getter
    private final List<String> disabled = new ArrayList<>();
    /**
     * 指定场景配置：指定场景下字段掩码映射关系，会覆盖global配置（注意是全覆盖，即按照场景维度覆盖，而非字段维度覆盖）
     */
    @Getter
    private final Map<String,Map<String,String>> specified = new HashMap<>();
    /**
     * 场景路径匹配器
     */
    private AntPathMatcher pathMatcher;
    /**
     * 场景路径分割符：默认是 /
     */
    @Getter
    private String pathSeparator;
    public void setPathSeparator(String pathSeparator) {
        this.pathSeparator = pathSeparator;
        this.pathMatcher = new AntPathMatcher(StrUtil.blankToDefault(pathSeparator,"/"));
    }

    public boolean matchDisabled(String scene){
        return this.disabled.stream().anyMatch(each -> pathMatcher.match(each, scene));
    }

    public String matchSpecified(String scene,String name){
        Map<String,String> val = this.specified.get(scene);
        if(val == null || val.isEmpty()){return null;}
        return StrUtil.blankToDefault(val.get(name),FIELD_NO_EXISTS);
    }

    public String matchGlobal(String name){
        return StrUtil.blankToDefault(this.global.get(name),null);
    }
}
