package com.pig4cloud.pigx.common.sensitive.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 敏感词配置类
 *
 * @author lengleng
 * @date 2024/3/26
 * <p>
 */
@Data
@ConfigurationProperties(SensitiveWordsProperties.PREFIX)
public class SensitiveWordsProperties {

    public static final String PREFIX = "pigx.sensitive.word";

    /**
     * The sensitive words to detect.
     */
    private String mask = "*";

    /**
     * Whether the sensitive words detection is enabled.
     */
    private boolean enabled = true;

    /**
     * Whether to ignore case when detecting sensitive words.
     */
    private boolean ignoreCase = true;

    /**
     * Whether to ignore width (full-width and half-width characters) when detecting
     * sensitive words.
     */
    private boolean ignoreWidth = true;

    /**
     * Whether to ignore number style (Arabic numerals, Roman numerals, etc.) when
     * detecting sensitive words.
     */
    private boolean ignoreNumStyle = true;

    /**
     * Whether to ignore Chinese style (simplified and traditional) when detecting
     * sensitive words.
     */
    private boolean ignoreChineseStyle = true;

    /**
     * Whether to ignore English style (US and UK spelling) when detecting sensitive
     * words.
     */
    private boolean ignoreEnglishStyle = true;

    /**
     * Whether to ignore repeated words when detecting sensitive words.
     */
    private boolean ignoreRepeat = true;

    /**
     * Whether to enable number check when detecting sensitive words.
     */
    private boolean enableNumCheck = false;

    /**
     * Whether to enable email check when detecting sensitive words.
     */
    private boolean enableEmailCheck = false;

    /**
     * Whether to enable URL check when detecting sensitive words.
     */
    private boolean enableUrlCheck = true;

    /**
     * The length of numbers to check when detecting sensitive words.
     */
    private int numCheckLen = 8;

    /**
     * The error message to return when sensitive words are detected.
     */
    private String errorMsg = "您的输入包含敏感词，请重新输入";

}
