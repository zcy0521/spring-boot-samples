package com.sample.springboot.view.velocity.tools;

import com.sample.springboot.view.velocity.enums.SampleEnum;
import org.apache.velocity.tools.config.DefaultKey;

@DefaultKey("enums")
public class EnumsTool {

    public String getSampleEnumLabel(Integer value) {
        if (null == value) {
            return "";
        }
        SampleEnum sampleEnum = SampleEnum.of(value);
        return sampleEnum.getLabel();
    }

}
