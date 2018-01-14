package com.github.pig.admin.common.config;

import com.github.pig.common.constant.SecurityConstants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author lengleng
 * @date 2017-12-21 21:12:18
 */
@Configuration
public class KaptchaConfig {

    private static final String KAPTCHA_BORDER = "kaptcha.border";
    private static final String KAPTCHA_TEXTPRODUCER_FONT_COLOR = "kaptcha.textproducer.font.color";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_SPACE = "kaptcha.textproducer.char.space";
    private static final String KAPTCHA_IMAGE_WIDTH = "kaptcha.image.width";
    private static final String KAPTCHA_IMAGE_HEIGHT = "kaptcha.image.height";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_LENGTH = "kaptcha.textproducer.char.length";
    private static final Object KAPTCHA_IMAGE_FONT_SIZE = "kaptcha.textproducer.font.size";

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put(KAPTCHA_BORDER, SecurityConstants.DEFAULT_IMAGE_BORDER);
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, SecurityConstants.DEFAULT_COLOR_FONT);
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, SecurityConstants.DEFAULT_CHAR_SPACE);
        properties.put(KAPTCHA_IMAGE_WIDTH, SecurityConstants.DEFAULT_IMAGE_WIDTH);
        properties.put(KAPTCHA_IMAGE_HEIGHT, SecurityConstants.DEFAULT_IMAGE_HEIGHT);
        properties.put(KAPTCHA_IMAGE_FONT_SIZE, SecurityConstants.DEFAULT_IMAGE_FONT_SIZE);
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, SecurityConstants.DEFAULT_IMAGE_LENGTH);
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
