<div align="center">

# Security Masks Set

**Java 可配置数据脱敏工具集**

一份 YAML 配置即可对接口返回中的敏感字段（姓名、手机号、身份证、银行卡）自动脱敏，业务代码零改动。

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-6DB33F?logo=springboot&logoColor=white)
![License](https://img.shields.io/badge/License-Apache_2.0-blue)
[![CI](https://github.com/hshen-g/security-masks-set/actions/workflows/ci.yml/badge.svg)](https://github.com/hshen-g/security-masks-set/actions/workflows/ci.yml)

English | [中文](README.zh-CN.md)

</div>

---

## 为什么做这个

数据合规（个保法 / GDPR）反复追问同一个问题：*这个接口是不是在泄露敏感字段？*常规修法是挨个改 DTO、改序列化器。本库反其道行之——业务代码一行不动，在一个配置文件里声明**哪个场景下哪个字段用哪种规则脱敏**，遍历和改写交给框架。

## 特性

- **场景化规则**——场景通常是 API 路径或接口方法名，支持 Ant 风格通配符（`/api/orders/*`）
- **三级优先级**——`disabled` > `specified` > `global`：全局开启、按场景微调、按场景豁免
- **按长度分段掩码**——保留头尾字符（如 `H3T4`），规则按值长度匹配，长短姓名可用不同策略
- **深度遍历**——基于 JSONPath 递归处理嵌套对象、集合、Map，原地改写字段值
- **内置规则**——客户姓名、手机号、身份证号、银行卡号，均可覆盖
- **可扩展**——实现 `LogicValuer` 接入自定义掩码逻辑
- **Spring / Spring Boot 友好**——提供自动装配，并内置可选 AOP 切面，自动拦截 `@RestController` 返回值

## 模块

| 模块 | 构件 | 说明 |
| --- | --- | --- |
| 核心 | `security-masks` | 掩码引擎、规则模型、内置规则 |
| Spring | `security-masks-spring` | 从 YAML 文件加载规则，适配非 Boot 的 Spring 应用 |
| Starter | `security-masks-springboot` | Spring Boot 自动装配 + 可选 REST AOP |

## 快速开始

> 构件暂未发布到 Maven Central，请先源码构建（`mvn clean install`，Java 8+）。

### Spring Boot

引入 `security-masks-springboot` 后在 `application.yml` 中配置：

```yaml
security-masks:
  placeholder: '*'
  rest-aop: true              # 自动拦截 @RestController 返回值
  classify:
    PHONE:
      - { retained: 'H3T4' }
    IDCARDNO:
      - { retained: 'H6T4' }
  reference:
    global:                   # 对所有场景生效
      custName: CNAME
      phone: PHONE
      idCardNo: IDCARDNO
      bankCardNo: BANKCARDNO
    disabled:                 # 这些场景不做掩码（Ant 通配符）
      - '/api/orders/*'
    specified:                # 按场景覆盖（场景维度整体覆盖）
      '/api/users/query':
        mobilePhone: PHONE
```

`rest-aop: true` 时，所有 `@RestController` 方法的返回值会在序列化前被递归脱敏，场景标识为控制器方法签名；设为 `false` 后可注入 `MaskHandler` 在任意位置手动调用。

### 普通写法

```java
MaskConfig config = /* 从 YAML 加载或代码构建 */;
MaskHandler handler = new MaskHandler(config);

handler.mask("any.scene", "phone", "13812345678");   // 138****5678
handler.mask(scene, responseBody);                   // 对整个对象深度脱敏
```

## 规则说明

一个**分类（classify）**是一组规则；每条规则按值长度匹配，并定义哪些字符保留：

```
retained: H3T4   →  保留前 3 位与后 4 位，中间填充占位符
len: '1,3'       →  该规则作用于长度 1–3 的值（'7,+' 表示 7 位及以上）
```

内置默认规则的实际效果：

| 字段 | 原值 | 脱敏后 |
| --- | --- | --- |
| 客户姓名（2 字） | 张三 | 张* |
| 客户姓名（4 字） | 欧阳敬我 | 欧阳** |
| 手机号 | 13812345678 | 138****5678 |
| 身份证号 | 411524199110126812 | 411***********812 |
| 银行卡号 | 6222020200112233 | ************2233 |

场景命中后，字段 → 分类的解析顺序为：场景 `disabled` → 场景 `specified` 映射 → `global` 映射；未解析到分类的字段保持原值。

## 扩展

实现 `LogicValuer` 即可接入自定义掩码策略：

```java
public class EmailMasker implements LogicValuer {
    public String classify() { return "EMAIL"; }
    public String evl(String placeholder, String value) { /* 你的掩码逻辑 */ return ...; }
}
```

注册为 `classify.EMAIL` 后，即可在任意 `global` / `specified` 映射中引用。

## 致谢

掩码引擎由 **liuyx**、**yuanx**、**Staunch** 开发，本仓库维护其开源发布。

## 开源协议

[Apache License 2.0](LICENSE)
