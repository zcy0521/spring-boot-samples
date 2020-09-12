package com.sample.springboot.cache.redis.mybatis.generator.plugin;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class ModelPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 自定义实体类
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // import JAP
        topLevelClass.addImportedType("javax.persistence.*");

        // @Table(name = `tableName`)
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");

        // import lombok
        topLevelClass.addImportedType("lombok.*");
        // @Data
        topLevelClass.addAnnotation("@Data");
        // @NoArgsConstructor
        topLevelClass.addAnnotation("@NoArgsConstructor");
        // @EqualsAndHashCode
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 自定义字段
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //@Column(name = `columnName`)
        String columnName = introspectedColumn.getActualColumnName();
        field.addAnnotation("@Column(name = \"" + getDelimiterName(columnName) + "\")");

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    private String getDelimiterName(String name) {
        // 获取属性beginningDelimiter
        String beginningDelimiter = getContext().getProperty("beginningDelimiter");
        if (StringUtils.isBlank(beginningDelimiter)) {
            throw new RuntimeException("请配置beginningDelimiter属性!");
        }

        // 获取属性endingDelimiter
        String endingDelimiter = getContext().getProperty("endingDelimiter");
        if (StringUtils.isBlank(endingDelimiter)) {
            throw new RuntimeException("请配置endingDelimiter属性!");
        }

        return beginningDelimiter + name + endingDelimiter;
    }
}
