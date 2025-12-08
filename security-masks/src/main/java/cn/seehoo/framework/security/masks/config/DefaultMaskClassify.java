package cn.seehoo.framework.security.masks.config;

import java.util.*;

/**
 * 缺省的掩码分类：客户名称（包含企业名称）、手机号、银行卡号、身份证号等
 * @author yuanx
 * @version 1.0
 * @date 2025/11/27 11:34
 * @since 1.0
 */
public final class DefaultMaskClassify {
    private static final Map<String, List<MaskClassify>> DEF_CLASSIFY = new HashMap<>();
    static {
        DEF_CLASSIFY.put("CNAME",cNames());
        DEF_CLASSIFY.put("PHONE",phones());
        DEF_CLASSIFY.put("BANKCARDNO",bankCardNo());
        DEF_CLASSIFY.put("IDCARDNO",idCardNo());
    }
    public static Map<String, List<MaskClassify>> get(){
        return Collections.unmodifiableMap(DEF_CLASSIFY);
    }
    private static List<MaskClassify> idCardNo(){
        List<MaskClassify> list = new ArrayList<>();
        MaskClassify one = new MaskClassify();
        one.setRetained("H3T3");
        list.add(one);
        return list;
    }
    private static List<MaskClassify> bankCardNo(){
        List<MaskClassify> list = new ArrayList<>();
        MaskClassify one = new MaskClassify();
        one.setRetained("T4");
        list.add(one);
        return list;
    }
    private static List<MaskClassify> phones(){
        List<MaskClassify> list = new ArrayList<>();
        MaskClassify one = new MaskClassify();
        one.setRetained("H3T4");
        list.add(one);
        return list;
    }
    private static List<MaskClassify> cNames(){
        List<MaskClassify> list = new ArrayList<>();
        MaskClassify one = new MaskClassify();
        one.setLen("1,3");
        one.setRetained("H1");
        list.add(one);
        MaskClassify two = new MaskClassify();
        two.setLen("4,4");
        two.setRetained("H2");
        list.add(two);
        MaskClassify three = new MaskClassify();
        three.setLen("5,6");
        three.setRetained("H2T2");
        list.add(three);
        MaskClassify four = new MaskClassify();
        four.setLen("7,+");
        four.setRetained("H4T2");
        list.add(four);
        return list;
    }
}
