package com.pig4cloud.pigx.common.api.encrypt.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.symmetric.AES;
import com.pig4cloud.pigx.common.api.encrypt.bean.CryptoInfoBean;
import com.pig4cloud.pigx.common.api.encrypt.config.ApiEncryptProperties;
import com.pig4cloud.pigx.common.api.encrypt.core.ISecretKeyResolver;
import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import com.pig4cloud.pigx.common.api.encrypt.exception.EncryptBodyFailException;
import com.pig4cloud.pigx.common.api.encrypt.exception.EncryptMethodNotFoundException;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * 辅助检测工具类
 * </p>
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/7
 */
@UtilityClass
public class ApiCryptoUtil {

    /**
     * SM4 密钥 HEX 字符串长度，对应 16 字节（128 bit）原始密钥。
     */
    private static final int SM4_HEX_KEY_LENGTH = 32;

    /**
     * 密钥提取器
     */
    public static final Map<EncryptType, Function<ApiEncryptProperties, String>> KEY_EXTRACTORS = Map.of(
            EncryptType.AES, ApiEncryptProperties::getAesKey,
            EncryptType.RSA, ApiEncryptProperties::getRsaPrivateKey,
            EncryptType.DES, ApiEncryptProperties::getDesKey,
            EncryptType.SM4, ApiEncryptProperties::getSm4Key
    );


    /**
     * 选择加密方式并进行加密
     *
     * @param jsonData json 数据
     * @param infoBean 加密信息
     * @return 加密结果
     */
    public static String encryptData(byte[] jsonData, CryptoInfoBean infoBean) {
        EncryptType type = infoBean.getType();
        if (type == null) {
            throw new EncryptMethodNotFoundException();
        }

        String secretKey = infoBean.getSecretKey();
        if (StrUtil.isBlank(secretKey)) {
            secretKey = SpringContextHolder.getBean(ISecretKeyResolver.class).getSecretKey(WebUtils.getRequest(), type);
        }
        Assert.hasText(secretKey, type + " key is not configured (未配置" + type + ")");

        if (type == EncryptType.DES) {

            return SecureUtil.des(secretKey.getBytes(StandardCharsets.UTF_8)).encryptBase64(jsonData);
        }
        if (type == EncryptType.AES) {
            return buildAes(secretKey).encryptBase64(jsonData);
        }
        if (type == EncryptType.RSA) {
            return SecureUtil.rsa(secretKey.getBytes(StandardCharsets.UTF_8), null)
                    .encryptBase64(jsonData, KeyType.PrivateKey);
        }

        if (type == EncryptType.SM4) {
            return SmUtil.sm4(getSm4KeyBytes(secretKey)).encryptHex(jsonData);
        }
        throw new EncryptBodyFailException();
    }

    /**
     * 选择加密方式并进行解密
     *
     * @param bodyData 密文字节数据
     * @param infoBean 加密类型和密钥信息
     * @return 解密后的原始字节数据
     */
    public static byte[] decryptData(byte[] bodyData, CryptoInfoBean infoBean) {
        EncryptType type = infoBean.getType();
        if (type == null) {
            throw new EncryptMethodNotFoundException();
        }

        String secretKey = infoBean.getSecretKey();
        if (StrUtil.isBlank(secretKey)) {
            secretKey = SpringContextHolder.getBean(ISecretKeyResolver.class).getSecretKey(WebUtils.getRequest(), type);
        }
        Assert.hasText(secretKey, type + " key is not configured (未配置" + type + ")");

        if (type == EncryptType.AES) {
            return buildAes(secretKey).decrypt(StrUtil.str(bodyData, StandardCharsets.UTF_8));
        }
        if (type == EncryptType.DES) {
            return SecureUtil.des(secretKey.getBytes(StandardCharsets.UTF_8)).decrypt(bodyData);
        }
        if (type == EncryptType.RSA) {
            return SecureUtil.rsa(secretKey.getBytes(StandardCharsets.UTF_8), null)
                    .decrypt(bodyData, KeyType.PrivateKey);
        }

        if (type == EncryptType.SM4) {
            return SmUtil.sm4(getSm4KeyBytes(secretKey))
                    .decryptStr(StrUtil.str(bodyData, StandardCharsets.UTF_8))
                    .getBytes(StandardCharsets.UTF_8);
        }

        throw new EncryptMethodNotFoundException();
    }

    /**
     * 解密字符串形式的密文数据。
     *
     * @param bodyData 字符串密文，按 UTF-8 转换为字节数组后解密
     * @param infoBean 加密类型和密钥信息
     * @return UTF-8 字符串形式的解密结果
     */
    public static String decryptData(String bodyData, CryptoInfoBean infoBean) {
        return StrUtil.str(decryptData(bodyData.getBytes(StandardCharsets.UTF_8), infoBean), StandardCharsets.UTF_8);
    }

    /**
     * 构建与前端约定的 AES 加解密因子：CFB 模式 + NoPadding，
     * 同时使用密钥的 UTF-8 字节作为 Key 与 IV，避免平台默认字符集差异。
     *
     * @param secretKey AES 密钥字符串
     * @return 已配置好 Key 与 IV 的 AES 实例
     */
    private static AES buildAes(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new AES(Mode.CFB, Padding.NoPadding,
                new SecretKeySpec(keyBytes, "AES"),
                new IvParameterSpec(keyBytes));
    }

    /**
     * 校验并解析 SM4 HEX 密钥。
     *
     * @param secretKey SM4 密钥，必须是 32 位 HEX 字符串（对应 16 字节 / 128 bit）
     * @return SM4 原始密钥字节
     */
    private static byte[] getSm4KeyBytes(String secretKey) {
        Assert.isTrue(secretKey.length() == SM4_HEX_KEY_LENGTH && HexUtil.isHexNumber(secretKey),
                EncryptType.SM4 + " key must be 32 hex characters (SM4密钥必须为32位HEX字符串)");
        return HexUtil.decodeHex(secretKey);
    }

}
