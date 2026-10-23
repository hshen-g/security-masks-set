package io.github.hsheng.security.masks;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@SpringBootTest
@Slf4j
public class WebApplicationTests {
//    @Autowired
//    private MaskHandler handler;
    @Test
    public void contextLoads(){
        MainCust cust = new MainCust();
        cust.setCustName("刘好好");
        cust.setAge(1);
        cust.setBalance(new BigDecimal("10000"));
        cust.setIdCardNo("411524199110126812");
//        handler.mask("/api/users/query",cust);
        log.info("masked val is {}", JSONObject.toJSONString(cust));
    }
}
