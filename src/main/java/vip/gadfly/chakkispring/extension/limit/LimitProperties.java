package vip.gadfly.chakkispring.extension.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Gadfly
 */

@Component
@ConfigurationProperties(prefix = "lin.cms.limit")
@PropertySource(value = "classpath:vip/gadfly/chakkispring/extension/limit/config.properties", encoding = "UTF-8")
public class LimitProperties {

    private Integer value = 20;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
