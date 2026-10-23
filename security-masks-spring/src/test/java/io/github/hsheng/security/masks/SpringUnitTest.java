package io.github.hsheng.security.masks;

import io.github.hsheng.security.masks.spring.ApiSecurityMasksAutoConfiguration;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * @author Staunch
 * @date 2022/8/12 14:22
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {InitConfig.class,ApiSecurityMasksAutoConfiguration.class})
@Slf4j
public class SpringUnitTest {
    @Autowired
    private MaskHandler handler;
    @Test
    public void test() {
        MainCust cust = new MainCust();
        cust.setCustName("刘好好");
        cust.setAge(1);
        cust.setBalance(new BigDecimal("10000"));
        cust.setIdCardNo("411524199110126812");
        handler.mask("/api/users/query11",cust);
        log.info("masked val is {}", JSONObject.toJSONString(cust));
    }
}
