package io.github.hsheng.security.masks;

import cn.hutool.core.map.ReferenceConcurrentMap;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.ReferenceUtil;
import cn.hutool.core.util.StrUtil;
import io.github.hsheng.security.masks.config.MaskClassify;
import io.github.hsheng.security.masks.config.MaskConfig;
import com.alibaba.fastjson2.JSONPath;

import java.util.List;
import java.util.Map;

/**
 * @author liuyx
 * @version 1.0
 * @date 2025/7/31 15:35
 * @since 1.0
 */
public class MaskHandler {
    private static final Map<String, JSONPath> CACHE_PATHS = new ReferenceConcurrentMap<>(new SafeConcurrentHashMap<>(256), ReferenceUtil.ReferenceType.SOFT);
    private final MaskConfig maskConfig;

    public MaskHandler(MaskConfig maskConfig) {
        this.maskConfig = maskConfig;
        this.maskConfig.init();
    }

    /**
     * 掩码处理：指定场景下，指定字段的值
     * @param scene 场景标识，一般是api路径
     * @param name 字段标识
     * @param value 字段的值
     * @return 掩码后的值
     */
    public String mask(String scene,String name,String value){
        return mask(maskConfig.findMask(scene,name),value);
    }

    /**
     * 掩码处理：指定场景下，指定对象
     * 注意：当前方法会直接修改传入对象中命中掩码规则的字段的值
     * @param scene 场景标识，一般是api路径
     * @param bean 待处理对象
     */
    public void mask(String scene,Object bean) {
        Map<String,Object> paths = JSONPath.paths(bean);
        for (String path : paths.keySet()) {
            Object val = paths.get(path);
            if(val instanceof String){
                String mask = maskConfig.findMask(scene,extraName(path));
                if(mask != null){
                    String maskVal = mask(mask,(String)val);
                    setByPath(path,bean,maskVal);
                }
            }
        }
    }

    /**
     * 掩码处理：指定掩码分类
     * @param mask 掩码分类
     * @param value 待掩码的值
     * @return 掩码后的值
     */
    public String mask(String mask,String value){
        if(StrUtil.isBlank(mask)){return value;}
        if(StrUtil.isBlank(value)){return value;}
        List<MaskClassify> classifies = maskConfig.getClassify().get(mask);
        if(classifies == null){return value;}
        for(MaskClassify each:classifies){
            String maskVal = each.mask(maskConfig.getPlaceholder(),value);
            if(maskVal != null){return maskVal;}
        }
        return value;
    }
    private String extraName(String path){
        return StrUtil.subAfter(path,'.',true);
    }

    private static void setByPath(String path,Object dest, Object value) {
        CACHE_PATHS.computeIfAbsent(path,(k)-> JSONPath.of(path)).set(dest, value);
    }
}
