package com.pig4cloud.pigx.common.api.encrypt.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.symmetric.AES;
import com.pig4cloud.pigx.common.api.encrypt.annotation.decrypt.ApiDecrypt;
import com.pig4cloud.pigx.common.api.encrypt.annotation.encrypt.ApiEncrypt;
import com.pig4cloud.pigx.common.api.encrypt.bean.CryptoInfoBean;
import com.pig4cloud.pigx.common.api.encrypt.core.ISecretKeyResolver;
import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import com.pig4cloud.pigx.common.api.encrypt.exception.EncryptBodyFailException;
import com.pig4cloud.pigx.common.api.encrypt.exception.EncryptMethodNotFoundException;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import lombok.experimental.UtilityClass;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
	 * 获取方法控制器上的加密注解信息
	 * @param methodParameter 控制器方法
	 * @return 加密注解信息
	 */
	@Nullable
	public static CryptoInfoBean getEncryptInfo(MethodParameter methodParameter) {
		ApiEncrypt encryptBody = AnnotatedElementUtils.findMergedAnnotation(methodParameter.getMethod(),
				ApiEncrypt.class);
		if (encryptBody == null) {
			return null;
		}
		return new CryptoInfoBean(encryptBody.value(), encryptBody.secretKey());
	}

	/**
	 * 获取方法控制器上的解密注解信息
	 * @param methodParameter 控制器方法
	 * @return 加密注解信息
	 */
	@Nullable
	public static CryptoInfoBean getDecryptInfo(MethodParameter methodParameter) {
		ApiDecrypt decryptBody = AnnotatedElementUtils.findMergedAnnotation(methodParameter.getMethod(),
				ApiDecrypt.class);
		if (decryptBody == null) {
			return null;
		}
		return new CryptoInfoBean(decryptBody.value(), decryptBody.secretKey());
	}

	/**
	 * 选择加密方式并进行加密
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
			// 构建前端对应解密AES 因子
			AES aes = new AES(Mode.CFB, Padding.NoPadding, new SecretKeySpec(secretKey.getBytes(), "AES"),
					new IvParameterSpec(secretKey.getBytes()));

			return aes.encryptBase64(jsonData);
		}
		if (type == EncryptType.RSA) {
			return SecureUtil.rsa(secretKey.getBytes(StandardCharsets.UTF_8), null)
				.encryptBase64(jsonData, KeyType.PrivateKey);
		}

		if (type == EncryptType.SM4) {
			return SmUtil.sm4(HexUtil.decodeHex(secretKey)).encryptHex(jsonData);
		}
		throw new EncryptBodyFailException();
	}

	/**
	 * 选择加密方式并进行解密
	 * @param bodyData byte array
	 * @param infoBean 加密信息
	 * @return 解密结果
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

			AES aes = new AES(Mode.CFB, Padding.NoPadding,
					new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES"),
					new IvParameterSpec(secretKey.getBytes(StandardCharsets.UTF_8)));
			return aes.decrypt(StrUtil.str(bodyData, StandardCharsets.UTF_8));

		}
		if (type == EncryptType.DES) {
			return SecureUtil.des(secretKey.getBytes(StandardCharsets.UTF_8)).decrypt(bodyData);
		}
		if (type == EncryptType.RSA) {
			return SecureUtil.rsa(secretKey.getBytes(StandardCharsets.UTF_8), null)
				.decrypt(bodyData, KeyType.PrivateKey);
		}

		if (type == EncryptType.SM4) {
			return SmUtil.sm4(HexUtil.decodeHex(secretKey))
				.decryptStr(StrUtil.str(bodyData, Charset.defaultCharset()))
				.getBytes();
		}

		throw new EncryptMethodNotFoundException();
	}

}
