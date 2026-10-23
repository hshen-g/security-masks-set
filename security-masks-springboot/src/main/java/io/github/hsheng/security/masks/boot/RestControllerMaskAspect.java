package io.github.hsheng.security.masks.boot;

import cn.hutool.core.util.ClassUtil;
import io.github.hsheng.security.masks.MaskHandler;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yuanx
 * @version 1.0
 * @date 2025/8/16 11:13
 * @since 1.0
 */
@Aspect
@AllArgsConstructor
public class RestControllerMaskAspect {
    private final MaskHandler maskHandler;
    @AfterReturning(value = "@within(org.springframework.web.bind.annotation.RestController)",
            returning = "result")
    public void around(JoinPoint joinPoint,Object result) {
        if(result == null){return;}
        if(ClassUtil.isSimpleTypeOrArray(result.getClass())){return;}
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        MergedAnnotations annotations = MergedAnnotations.from(signature.getMethod(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
        boolean isApi = annotations.isPresent(RequestMapping.class);
        if(isApi){
            maskHandler.mask(signature.toString(),result);
        }
    }
}
