/**
 * PIG 验证码安全能力模块。
 *
 * <p>
 * 该包集中封装图形验证码与行为验证码的校验入口，统一处理 blockPuzzle、clickWord、math 等验证码类型，避免认证服务、用户中心等业务模块重复拼装校验逻辑。
 * </p>
 *
 * <p>
 * 同时提供 anji-captcha 的 Redis 缓存 SPI 实现，让行为验证码在多实例部署时共享缓存数据， 保持验证码生成、校验和过期删除的一致性。
 * </p>
 *
 * @author lengleng
 * @date 2026-05-20
 */
package com.pig4cloud.pig.common.security.captcha;
