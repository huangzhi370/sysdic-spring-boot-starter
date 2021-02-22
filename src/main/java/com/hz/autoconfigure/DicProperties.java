package com.hz.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangzhi
 */
@Data
@ConfigurationProperties(prefix = "sys.dic")
public class DicProperties {

    private Boolean isEnable;
}
