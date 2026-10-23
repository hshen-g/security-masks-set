package io.github.hsheng.security.masks;

import cn.hutool.core.io.resource.ResourceUtil;
import io.github.hsheng.security.masks.config.MaskConfig;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * 配置文件中定义的分类规则应优先于内置默认规则
 * @author hshen-g
 * @version 1.0
 * @since 1.0
 */
public class ConfigOverrideTest {
    @Test
    public void test() throws Exception {
        MaskConfig maskConfig;
        try(InputStream inputStream = ResourceUtil.getStream("security-masks.yml")){
            maskConfig = new YAMLMapper().readValue(inputStream, MaskConfig.class);
        }
        MaskHandler handler = new MaskHandler(maskConfig);
        // security-masks.yml 中 IDCARDNO 定义为 H6T4，不应被内置默认 H3T3 覆盖
        String masked = handler.mask("any.scene", "idCardNo", "411524199110126812");
        Assert.assertEquals("411524********6812", masked);
    }
}
