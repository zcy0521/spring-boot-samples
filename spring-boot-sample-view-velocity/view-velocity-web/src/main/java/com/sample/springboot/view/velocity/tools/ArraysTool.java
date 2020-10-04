package com.sample.springboot.view.velocity.tools;

import com.google.common.base.Joiner;
import org.apache.velocity.tools.config.DefaultKey;

import static java.util.Arrays.asList;

@DefaultKey("arrays")
public class ArraysTool {

    private static final String DELIMITER = ", ";

    public String toString(Object[] arrays) {
        if (null == arrays || arrays.length < 1) {
            return "";
        }
        return Joiner.on(DELIMITER).skipNulls().join(asList(arrays));
    }

}
