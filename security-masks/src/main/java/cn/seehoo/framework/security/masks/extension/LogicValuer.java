package cn.seehoo.framework.security.masks.extension;

/**
 * 自定义掩码处理类
 * @author yuanx
 * @version 1.0
 * @date 2025/11/27 15:08
 * @since 1.0
 */
public interface LogicValuer {
    /**
     * 掩码处理
     * @param placeholder 占位符
     * @param value 原值
     * @return 处理后值
     */
    String evl(String placeholder, String value);

    /**
     * 掩码分类
     * @return 掩码分类
     */
    String classify();
}
