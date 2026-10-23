package io.github.hsheng.security.masks;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuanx
 * @version 1.0
 * @date 2025/8/6 14:50
 * @since 1.0
 */
@Data
public class MainCust {
    private String custName;
    private int age;
    private String idCardNo;
    private BigDecimal balance;
    private List<Students> students;
}
