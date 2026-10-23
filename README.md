<div align="center">

# Security Masks Set

**Configurable data masking toolkit for Java**

Mask sensitive fields (names, phone numbers, ID numbers, bank cards) in API responses with a single YAML config — no changes to your business code.

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-6DB33F?logo=springboot&logoColor=white)
![License](https://img.shields.io/badge/License-Apache_2.0-blue)
[![CI](https://github.com/hshen-g/security-masks-set/actions/workflows/ci.yml/badge.svg)](https://github.com/hshen-g/security-masks-set/actions/workflows/ci.yml)

English | [中文](README.zh-CN.md)

</div>

---

## Why

Data-compliance work (PIPL / GDPR) keeps asking the same question: *is this API leaking sensitive fields?* Fixing it usually means touching every DTO and every serializer. This library takes the other approach — keep the code untouched, declare **which field gets masked in which scene** in one config file, and let the framework do the traversal.

## Features

- **Scene-based rules** — a scene is usually an API path or a facade method name; rules support Ant-style wildcards (`/api/orders/*`)
- **Three-level priority** — `disabled` > `specified` > `global`: enable masking globally, fine-tune per scene, or opt out per scene
- **Length-aware masking** — keep head/tail characters (e.g. `H3T4`), classify rules match by value length so short and long names can be masked differently
- **Deep traversal** — walks nested objects, collections and maps via JSONPath, and rewrites values in place
- **Built-in rules** — customer name, phone number, ID card number, bank card number; fully overridable
- **Extensible** — implement `LogicValuer` to plug in custom masking logic
- **Spring / Spring Boot friendly** — auto-configuration included, plus an optional AOP aspect that masks every `@RestController` return value automatically

## Modules

| Module | Artifact | Purpose |
| --- | --- | --- |
| core | `security-masks` | Masking engine, rule model, built-in rules |
| spring | `security-masks-spring` | Loads rules from a YAML file for plain Spring apps |
| starter | `security-masks-springboot` | Spring Boot auto-configuration + optional REST AOP |

## Getting Started

> Artifacts are not yet published to Maven Central — build from source for now (`mvn clean install`, Java 8+).

### Spring Boot

Drop `security-masks-springboot` on the classpath and configure in `application.yml`:

```yaml
security-masks:
  placeholder: '*'
  rest-aop: true              # mask @RestController return values automatically
  classify:
    PHONE:
      - { retained: 'H3T4' }
    IDCARDNO:
      - { retained: 'H6T4' }
  reference:
    global:                   # applies to every scene
      custName: CNAME
      phone: PHONE
      idCardNo: IDCARDNO
      bankCardNo: BANKCARDNO
    disabled:                 # these scenes are left untouched (Ant patterns)
      - '/api/orders/*'
    specified:                # per-scene overrides (whole-scene granularity)
      '/api/users/query':
        mobilePhone: PHONE
```

With `rest-aop: true`, every `@RestController` method return value is walked and masked before serialization; the scene is the controller method signature. Turn the aspect off (`rest-aop: false`) and inject `MaskHandler` wherever you want to mask manually.

### Plain Java

```java
MaskConfig config = /* load from YAML or build in code */;
MaskHandler handler = new MaskHandler(config);

handler.mask("any.scene", "phone", "13812345678");   // 138****5678
handler.mask(scene, responseBody);                   // deep-masks a whole object
```

## How Rules Work

A **classify** is a list of rules; each rule matches by value length and defines which characters survive:

```
retained: H3T4   →  keep first 3 and last 4 chars, fill the middle with the placeholder
len: '1,3'       →  rule applies to values of length 1–3 ('7,+' means 7 or longer)
```

Example effect (built-in defaults):

| Field | Original | Masked |
| --- | --- | --- |
| Customer name (2 chars) | 张三 | 张* |
| Customer name (4 chars) | 欧阳敬我 | 欧阳** |
| Phone | 13812345678 | 138****5678 |
| ID card | 411524199110126812 | 411***********812 |
| Bank card | 6222020200112233 | ************2233 |

When a scene is matched, field → classify lookups resolve in order: scene `disabled` → scene `specified` mapping → `global` mapping. A field that resolves to no classify stays untouched.

## Extending

Implement `LogicValuer` to add a custom masking strategy:

```java
public class EmailMasker implements LogicValuer {
    public String classify() { return "EMAIL"; }
    public String evl(String placeholder, String value) { /* mask it */ return ...; }
}
```

Register it under `classify.EMAIL` and reference it from any `global` / `specified` mapping.

## Acknowledgements

The masking engine was originally developed by **liuyx**, **yuanx** and **Staunch**; this repository maintains the open-source release.

## License

[Apache License 2.0](LICENSE)
