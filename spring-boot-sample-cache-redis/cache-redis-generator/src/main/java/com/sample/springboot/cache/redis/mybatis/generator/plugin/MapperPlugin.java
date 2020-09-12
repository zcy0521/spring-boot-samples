package com.sample.springboot.cache.redis.mybatis.generator.plugin;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;

import java.util.List;
import java.util.Properties;

public class MapperPlugin extends PluginAdapter {

    private String baseMapper;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        // 设置 baseMapper
        String baseMapper = this.properties.getProperty("baseMapper");
        if (StringUtils.isBlank(baseMapper)) {
            throw new RuntimeException("请配置MapperPlugin插件baseMapper属性!");
        }
        this.baseMapper = baseMapper;
    }



    /**
     * 自定义Mapper
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // import接口
        interfaze.addImportedType(new FullyQualifiedJavaType(baseMapper));

        // import实体类
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        interfaze.addImportedType(entityType);

        // extends BaseMapper<DO>
        interfaze.addSuperInterface(new FullyQualifiedJavaType(baseMapper + "<" + entityType.getShortName() + ">"));

        return super.clientGenerated(interfaze, introspectedTable);
    }

    /**
     * selectAll
     */
    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * selectByPrimaryKey
     */
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * insert
     */
    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * updateByPrimaryKey
     */
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * deleteByPrimaryKey
     */
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }
}
