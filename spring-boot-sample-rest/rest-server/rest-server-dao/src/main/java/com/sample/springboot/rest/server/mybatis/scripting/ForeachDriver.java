package com.sample.springboot.rest.server.mybatis.scripting;

import com.google.common.base.Splitter;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForeachDriver extends XMLLanguageDriver implements LanguageDriver {

    /**
     * IN (#{sampleEnums.value})
     */
    private static final Pattern PATTERN = Pattern.compile("IN \\(#\\{(.*?)}\\)");

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Matcher matcher = PATTERN.matcher(script);
        if (matcher.find()) {
            // 获取集合属性名
            String collectionName = matcher.group(1);
            String item = "item";
            // 集合属性为bean
            if (collectionName.contains(".")) {
                List<String> property = Splitter.on('.').trimResults().omitEmptyStrings().splitToList(collectionName);
                collectionName = property.get(0);
                item = "item." + property.get(1);
            }
            // 拼script
            script = matcher.replaceAll("IN <foreach collection=\"" + collectionName +"\" item=\"item\" open=\"(\" separator=\",\" close=\")\">#{" + item + "}</foreach>");
        }
        // 外层添加script标签
        script = "<script>" + script + "</script>";
        return super.createSqlSource(configuration, script, parameterType);
    }

}
