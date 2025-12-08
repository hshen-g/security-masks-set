package cn.seehoo.framework.security.masks;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.seehoo.framework.security.masks.config.MaskConfig;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Notice:
 *
 * @author Staunch
 * @version 1.0
 * @date 2021/4/14
 * @since 1.0
 */
public class SimpleTest {
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();
    @Test
    public void test() throws Exception{
        MaskConfig maskConfig;
        try(InputStream inputStream = ResourceUtil.getStream("security-masks.yml")){
            maskConfig = YAML_MAPPER.readValue(inputStream, MaskConfig.class);
        }
        MaskHandler handler = new MaskHandler(maskConfig);
        MainCust cust = new MainCust();
        List< Students> students = new ArrayList<>();
        students.add(new Students("欧阳敬我", new AAA()));
        students.add(new Students("张三", new AAA()));
        cust.setStudents(students);
        cust.setCustName("刘好好");
        cust.setAge(1);
        cust.setBalance(new BigDecimal("10000"));
        cust.setIdCardNo("411524199110126812");
        handler.mask("com.eigpay.leasing.cust.provider.CustomerBasicFacade.pageQuery", cust);
        System.out.println(JSONObject.toJSONString(cust));
    }
}
