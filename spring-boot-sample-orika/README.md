# Orika

[Orika](https://orika-mapper.github.io)

[Orika Doc](https://orika-mapper.github.io/orika-docs)

[Orika Git](https://github.com/orika-mapper/orika)

## Spring Boot 集成

[Orika Doc](https://orika-mapper.github.io/orika-docs/intro.html)

[maven](https://search.maven.org/artifact/ma.glasnost.orika/orika-core)

### 配置 `pom`

```xml
<properties>
    <orika-core.version>1.4.2</orika-core.version>
</properties>
...
<dependency>
   <groupId>ma.glasnost.orika</groupId>
   <artifactId>orika-core</artifactId>
   <version>${orika-core.version}</version>
</dependency>
```

### Orika配置

- `OrikaConfig.java`

```java
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory getFactory() {
        return new DefaultMapperFactory.Builder().build();
    }
}
```

## Mapper

### 属性名相同

[Introduction](https://orika-mapper.github.io/orika-docs/intro.html)

- `map(source, targetType.class)`

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleVO from(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public SampleDO to(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }
}
```

### 属性名不同

[Introduction](https://orika-mapper.github.io/orika-docs/intro.html)

- `.field()`注册字段映射

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .field("sourceField", "targetField")
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleVO from(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public SampleDO to(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }
}
```

### 需要忽略的属性

[Declarative Mapping Configuration](https://orika-mapper.github.io/orika-docs/mappings-via-classmapbuilder.html)

- `.fieldAToB()` `.fieldBToA()` 单向映射

- `exclude()` 排除字段 需要字段同时存在于source target

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .fieldAToB("sourceField", "targetField")
                .fieldBToA("sourceField", "targetField")
                .exclude("excludeField")
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleVO from(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public SampleDO to(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }
}
```

### 属性类型不同 自定义Converter

[Custom Converters](https://orika-mapper.github.io/orika-docs/converters.html)

- 编写SampleEnumConverter

```java
public class SampleEnumConverter extends BidirectionalConverter<SampleEnum, Integer> {

    @Override
    public Integer convertTo(SampleEnum source, Type<Integer> type) {
        if (null == source) {
            return null;
        }
        return source.getValue();
    }

    @Override
    public SampleEnum convertFrom(Integer source, Type<SampleEnum> type) {
        if (null == source) {
            return null;
        }

        SampleEnum sampleEnum = SampleEnum.of(source);
        if (null == sampleEnum) {
            throw new IllegalArgumentException(String.format("Cannot convert SampleEnum of value: %s", source));
        }
        return sampleEnum;
    }
}
```

- 全局注册Converter

```java
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory getFactory() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        // 注册全局 converter
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new SampleEnumConverter());
        return mapperFactory;
    }
}
```

- Mapper注册Converter

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter("sampleEnumConverter", new SampleEnumConverter());
        
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .fieldMap("sampleEnum", "sampleEnum").converter("sampleEnumConverter").add()
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleVO from(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public SampleDO to(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }
}
```

### 自定义转换规则

[Advanced Mapping Configurations](https://orika-mapper.github.io/orika-docs/advanced-mappings.html)

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .byDefault()
                .customize(new CustomMapper<SampleDO, SampleVO>() {
                    @Override
                    public void mapAtoB(SampleDO source, SampleVO target, MappingContext context) {
                        // 自定义规则
                    }
                    @Override
                    public void mapBtoA(SampleVO source, SampleDO target, MappingContext context) {
                        // 自定义规则
                    }
                })
                .register();
        this.mapperFactory = mapperFactory;
    }

    public SampleVO from(SampleDO sample) {
        return mapperFactory.getMapperFacade().map(sample, SampleVO.class);
    }

    public SampleDO to(SampleVO sampleVO) {
        return mapperFactory.getMapperFacade().map(sampleVO, SampleDO.class);
    }
}
```

### 集合

- `mapAsList(sourceList, targetType.class)`

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public List<SampleVO> listFrom(List<SampleDO> samples) {
        return mapperFactory.getMapperFacade().mapAsList(samples, SampleVO.class);
    }

    public List<SampleDO> listTo(List<SampleVO> sampleVOs) {
        return mapperFactory.getMapperFacade().mapAsList(sampleVOs, SampleDO.class);
    }
}
```

### 更新

- `OrikaConfig.java`

```java
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory getFactory() {
        return new DefaultMapperFactory.Builder().mapNulls(false).build();
    }
}
```

- `map(source, target)`

```java
@Component
public class SampleVOMapper {

    private MapperFactory mapperFactory;

    @Autowired
    public SampleVOMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(SampleDO.class, SampleVO.class)
                .byDefault()
                .register();
        this.mapperFactory = mapperFactory;
    }

    public void updateFrom(SampleDO source, SampleVO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }

    public void updateTo(SampleVO source, SampleDO target) {
        mapperFactory.getMapperFacade().map(source, target);
    }
}
```
