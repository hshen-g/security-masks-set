package io.github.hsheng.security.masks.config;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 掩码配置
 * @author yuanx
 * @version 1.0
 * @date 2025/8/5 11:00
 * @since 1.0
 */
@Getter
public class MaskConfig {
    /**
     * 掩码符号：默认是*
     */
    private String placeholder;
    /**
     * 分类定义配置
     */
    private final Map<String, List<MaskClassify>> classify = new HashMap<>();
    /**
     * 引用定义配置：即什么场景（一般是API）使用什么掩码配置，优先级-- disabled>specified>global
     */
    @Setter
    private MaskReference reference = new MaskReference();

    public void setPlaceholder(String placeholder) {
        this.placeholder = StrUtil.blankToDefault(placeholder, "*");
    }
    public void init(){
        // 内置规则仅补缺，不覆盖已加载的用户配置
        DefaultMaskClassify.get().forEach((k,v) -> this.classify.putIfAbsent(k,v));
    }
    /**
     * 查找掩码分类
     * @param scene 场景标识
     * @param name 字段标识
     * @return 对应的掩码分类
     */
    public String findMask(String scene, String name){
        MaskReference reference = this.reference;
        if(reference.matchDisabled(scene)){return null;}
        String mask = reference.matchSpecified(scene, name);
        if(mask == null){return reference.matchGlobal(name);}
        if(MaskReference.FIELD_NO_EXISTS.equals(mask)){return null;}
        return mask;
    }
}
