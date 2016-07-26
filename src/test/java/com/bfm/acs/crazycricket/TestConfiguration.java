/**
 * 
 */
package com.bfm.acs.crazycricket;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration class
 * 
 * @author Kapil
 * Created: Jul 27, 2016
 *
 */
@Configuration
public class TestConfiguration {
	@Bean
    public ErrorController errorController(ErrorAttributes errorAttributes) {
        return new ErrorController(errorAttributes);
    }
}
