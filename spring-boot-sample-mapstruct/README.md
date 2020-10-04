# MapStruct

[MapStruct](https://mapstruct.org)

[MapStruct Doc](https://mapstruct.org/documentation)

[MapStruct Git](https://github.com/mapstruct/mapstruct)


## IDE 插件

[IntelliJ IDEA](https://mapstruct.org/documentation/ide-support/#intellij-idea)

## Spring Boot 集成

[MapStruct Doc](https://mapstruct.org/documentation/installation/#apache-maven)

[maven](https://search.maven.org/artifact/org.mapstruct/mapstruct)

### 配置 `pom`

```xml
...
<properties>
    <org.mapstruct.version>1.4.0.Final</org.mapstruct.version>
</properties>
...
<dependencies>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
</dependencies>
...
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

## Mapper

### 属性名相同

[Basic mappings](https://mapstruct.org/documentation/stable/reference/html/#basic-mappings)

[Inverse mappings](https://mapstruct.org/documentation/stable/reference/html/#inverse-mappings)

```java
@Mapper(componentModel = "spring")
public interface SampleVOMapper {

    SampleVO from(SampleDO sample);
    
    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

> 反转配置`@InheritInverseConfiguration`与`@Mapping`同时使用时，只对同时配置了`source`和`target`的`@Mapping`生效。

### 属性名不同

[controlling-nested-bean-mappings](https://mapstruct.org/documentation/stable/reference/html/#controlling-nested-bean-mappings)

使用`@Mapping`并指定`source`和`target`

```java
@Mapper(componentModel = "spring")
public interface SampleVOMapper {

    @Mapping(source = "sourceProperty", target = "targetProperty")
    SampleVO from(SampleDO sample);
    
    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

### 需要忽略的属性

[controlling-nested-bean-mappings](https://mapstruct.org/documentation/stable/reference/html/#controlling-nested-bean-mappings)

使用`@Mapping`并指定`target`和`ignore = true`

```java
@Mapper(componentModel = "spring")
public interface SampleVOMapper {

    @Mapping(target = "ignoreProperty", ignore = true)
    SampleVO from(SampleDO sample);
    
    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

### 属性类型不同 部分类型可以互相转换

[Implicit type conversions](https://mapstruct.org/documentation/stable/reference/html/#implicit-type-conversions)

- `Date` `Time` `DateTime` to `String`
    - @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    - @Mapping(source = "sampleTime", target = "sampleTime", dateFormat = "HH:mm")
    - @Mapping(source = "sampleDatetime", target = "sampleDatetime", dateFormat = "yyyy-MM-dd'T'HH:mm")

- `BigDecimal` to `String`
    - @Mapping(source = "sampleAmount", target = "sampleAmount", numberFormat = "#.00")

```java
@Mapper(componentModel = "spring")
public interface SampleVOMapper {

    @Mapping(source = "sampleDate", target = "sampleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "sampleTime", target = "sampleTime", dateFormat = "HH:mm")
    @Mapping(source = "sampleDatetime", target = "sampleDatetime", dateFormat = "yyyy-MM-dd'T'HH:mm")
    @Mapping(source = "sampleAmount", target = "sampleAmount", numberFormat = "#.00")
    SampleVO from(SampleDO sample);

    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

### 属性类型不同 且无法自动转换的编写类型转换Mapper

[Invoking other mappers](https://mapstruct.org/documentation/stable/reference/html/#invoking-other-mappers)

- 编写SampleEnumMapper

```java
@Mapper(componentModel = "spring")
public class SampleEnumMapper {

    public Integer toInteger(SampleEnum sampleEnum) {
        if (null == sampleEnum) {
            return null;
        }
        return sampleEnum.getValue();
    }

    public SampleEnum fromInteger(Integer value) {
        if (null == value) {
            return null;
        }
        SampleEnum sampleEnum = SampleEnum.of(value);
        if (null == sampleEnum) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", value));
        }
        return sampleEnum;
    }
}
```

- 应用SampleEnumMapper `@Mapper(uses = {SampleEnumMapper.class})`

```java
@Mapper(componentModel = "spring",
        uses = {SampleEnumMapper.class}
)
public interface SampleVOMapper {

    SampleVO from(SampleDO sample);

    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

### 自定义转换规则

[Mapping customization with decorators](https://mapstruct.org/documentation/stable/reference/html/#customizing-mappers-using-decorators)

- 编写自定义装饰器

```java
public abstract class SampleVOMapperDecorator implements SampleVOMapper {

    @Autowired
    private SampleVOMapper sampleVOMapper;

    @Override
    public SampleVO from(SampleDO sample) {
        SampleVO sampleVO = sampleVOMapper.from(sample);
        // 自定义规则
        return sampleVO;
    }

    @Override
    public SampleDO to(SampleVO sampleVO) {
        SampleDO sample = sampleVOMapper.to(sampleVO);
        // 自定义规则
        return sample;
    }
}
```

- 应用装饰器 `@DecoratedWith(SampleMapperDecorator.class)`

```java
@Mapper(componentModel = "spring")
@DecoratedWith(SampleVOMapperDecorator.class)
public interface SampleVOMapper {

    SampleVO from(SampleDO sample);

    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
}
```

### 集合

[Mapping collections](https://mapstruct.org/documentation/stable/reference/html/#mapping-collections)

```java
@Mapper(componentModel = "spring")
public interface SampleVOMapper {

    SampleVO from(SampleDO sample);
    
    List<SampleVO> listFrom(List<SampleDO> samples);
    
    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
    
    List<SampleDO> listTo(List<SampleVO> sampleVOs);
}
```

### 更新

[Updating existing bean instances](https://mapstruct.org/documentation/stable/reference/html/#updating-bean-instances)

[Mapping configuration inheritance](https://mapstruct.org/documentation/stable/reference/html/#mapping-configuration-inheritance)

[Controlling mapping result for 'null' properties in bean mappings (update mapping methods only)](https://mapstruct.org/documentation/stable/reference/html/#mapping-result-for-null-properties)

```java
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SampleVOMapper {

    SampleVO from(SampleDO sample);
    
    @InheritConfiguration(name = "from")
    void updateFrom(SampleDO source, @MappingTarget SampleVO target);
    
    @InheritInverseConfiguration(name = "from")
    SampleDO to(SampleVO sampleVO);
    
    @InheritConfiguration(name = "to")
    void updateTo(UserDTO source, @MappingTarget User target);
}
```

> `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE`: 更新时，source的null属性不复制

> `@InheritConfiguration`: 指定继承的配置
