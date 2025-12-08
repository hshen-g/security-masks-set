package cn.seehoo.framework.security.masks.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 掩码分类定义：根据字段长度，定义掩码规则
 * @author yuanx
 * @version 1.0
 * @date 2025/8/5 17:56
 * @since 1.0
 */
public class MaskClassify {
    private final static String ZERO = "0";
    private final static String PLUS_SYMBOL = "+";
    private final static Pattern RETAINED_REGEX = Pattern.compile("^(H(?<h1>\\d+)T(?<t1>\\d+))|(H(?<h2>\\d+))|(T(?<t2>\\d+))$");
    private final static Pattern LEN_REGEX = Pattern.compile("^(?<min>[1-9]+),(?<max>[1-9]+|\\+)$");
    /**
     * 字段长度：'1,3' 表示字符串长度在1~3之间，'7,+' 表示字符串长度大于7， '4' 表示字符串长度等于4
     */
    @Getter
    private String len;
    private int lenMin = 1;
    private int lenMax = Integer.MAX_VALUE;
    /**
     * 保留字符：H4T2 表示保留头2个字符，尾2个字符，剩余的填充掩码符号
     */
    @Getter
    private String retained;
    private int heads;
    private int tails;

    public void setLen(String len) {
        this.len = len;
        if(len == null){return;}
        Matcher matcher = LEN_REGEX.matcher(len);
        Assert.isTrue(matcher.matches(),"classify属性[len]格式错误");
        String min = matcher.group("min");
        String max = matcher.group("max");
        lenMin = Integer.parseInt(min);
        if(!PLUS_SYMBOL.equals(max)){
            lenMax = Integer.parseInt(max);
        }
        Assert.isTrue(lenMax >= lenMin, "classify属性[len]配置错误：min>max");
    }

    /**
     * 设置保留字符配置
     * @param retained 保留字符
     */
    public void setRetained(String retained) {
        this.retained = retained;
        Assert.notNull(retained, "classify属性[retained]不能为空");
        Matcher matcher = RETAINED_REGEX.matcher(retained);
        Assert.isTrue(matcher.matches(),"classify属性[retained]格式错误");
        String h1 = matcher.group("h1");
        String t1 = matcher.group("t1");
        String h2 = matcher.group("h2");
        String t2 = matcher.group("t2");
        String h = h1 != null ? h1 : h2;
        String t = t1 != null ? t1 : t2;
        heads = Integer.parseInt(StrUtil.nullToDefault(h,ZERO));
        tails = Integer.parseInt(StrUtil.nullToDefault(t,ZERO));
    }

    public String mask(String placeholder, String value){
        StringBuilder newVal = new StringBuilder();
        int len = value.length();
        if(len < lenMin || len > lenMax){
            return null;
        }
        if(len <= heads + tails){
            return null;
        }
        int placeholderNum = len - heads - tails;
        if(heads > 0){
            newVal.append(value, 0, heads);
        }
        for(int i = 0; i < placeholderNum; i++){
            newVal.append(placeholder);
        }
        if(tails > 0){
            newVal.append(value, len - tails, len);
        }
        return newVal.toString();
    }
}
