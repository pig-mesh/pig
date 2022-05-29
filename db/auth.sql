SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
                                        `id` varchar(100) NOT NULL,
                                        `registered_client_id` varchar(100) NOT NULL,
                                        `principal_name` varchar(200) NOT NULL,
                                        `authorization_grant_type` varchar(100) NOT NULL,
                                        `attributes` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
                                        `state` varchar(500) DEFAULT NULL,
                                        `authorization_code_value` blob,
                                        `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
                                        `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
                                        `authorization_code_metadata` varchar(2000) DEFAULT NULL,
                                        `access_token_value` blob,
                                        `access_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `access_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `access_token_metadata` varchar(2000) DEFAULT NULL,
                                        `access_token_type` varchar(100) DEFAULT NULL,
                                        `access_token_scopes` varchar(1000) DEFAULT NULL,
                                        `oidc_id_token_value` blob,
                                        `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `oidc_id_token_metadata` varchar(2000) DEFAULT NULL,
                                        `refresh_token_value` blob,
                                        `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `refresh_token_metadata` varchar(2000) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
                                                `registered_client_id` varchar(100) NOT NULL,
                                                `principal_name` varchar(200) NOT NULL,
                                                `authorities` varchar(1000) NOT NULL,
                                                PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of oauth2_authorization_consent
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
                                            `id` varchar(100) NOT NULL,
                                            `client_id` varchar(100) NOT NULL,
                                            `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `client_secret` varchar(200) DEFAULT NULL,
                                            `client_secret_expires_at` timestamp NULL DEFAULT NULL,
                                            `client_name` varchar(200) NOT NULL,
                                            `client_authentication_methods` varchar(1000) NOT NULL,
                                            `authorization_grant_types` varchar(1000) NOT NULL,
                                            `redirect_uris` varchar(1000) DEFAULT NULL,
                                            `scopes` varchar(1000) NOT NULL,
                                            `client_settings` varchar(2000) NOT NULL,
                                            `token_settings` varchar(2000) NOT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------
BEGIN;
INSERT INTO `oauth2_registered_client` VALUES ('jumuning', 'jumuning', '2021-11-24 10:39:41', '{bcrypt}$2a$10$aNZ7R/TpKdRBrPT/gl7Avur0mj.1MAwbz47RT1Lm0sNZm51K4WFvC', NULL, 'jumuning', 'client_secret_post,client_secret_basic', 'refresh_token,client_credentials,password,authorization_code', 'https://www.baidu.com', 'message.read,role.admin', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}');
INSERT INTO `oauth2_registered_client` VALUES ('pig', 'pig', '2021-11-24 16:35:24', '{bcrypt}$2a$10$oKyVIM.bR8Bjt5PCMZzRJedqEfaQkUhfLkbxpNfM8xPS/JnjtVFZ2', NULL, 'pig', 'client_secret_post,client_secret_basic', 'refresh_token,client_credentials,password,authorization_code', 'https://pig4cloud.com', 'message.read,message.write', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":false}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",10800.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",10800.000000000]}');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
