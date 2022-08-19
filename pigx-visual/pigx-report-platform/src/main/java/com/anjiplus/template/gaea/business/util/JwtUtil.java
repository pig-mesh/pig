package com.anjiplus.template.gaea.business.util;

import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by raodeming on 2021/8/18.
 */
public class JwtUtil {

    private static final String JWT_SECRET = "aj-report";

    public static String createToken(String reportCode, String shareCode, Date expires) {
        return createToken(reportCode, shareCode, null, expires);
    }

    public static String createToken(String reportCode, String shareCode, String password, Date expires) {
        String token = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(expires)
                .withClaim("reportCode", reportCode)
                .withClaim("shareCode", shareCode)
                .withClaim("sharePassword", password)
                .sign(Algorithm.HMAC256(JWT_SECRET));
        return token;
    }


    public static Map<String, Claim> getClaim(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
            DecodedJWT decodedJwt = jwtVerifier.verify(token);
            return decodedJwt.getClaims();
        } catch (Exception e) {
            throw BusinessExceptionBuilder.build(ResponseCode.REPORT_SHARE_LINK_INVALID, e.getMessage());
        }
    }

    public static String getReportCode(String token) {
        Claim claim = getClaim(token).get("reportCode");
        if (null == claim) {
            throw BusinessExceptionBuilder.build(ResponseCode.REPORT_SHARE_LINK_INVALID);
        }
        return claim.asString();
    }

    public static String getShareCode(String token) {
        Claim claim = getClaim(token).get("shareCode");
        if (null == claim) {
            throw BusinessExceptionBuilder.build(ResponseCode.REPORT_SHARE_LINK_INVALID);
        }
        return claim.asString();
    }

    public static String getPassword(String token) {
        Claim claim = getClaim(token).get("sharePassword");
        if (null == claim) {
            return null;
        }
        if (StringUtils.isNotBlank(claim.asString())) {
            return claim.asString();
        }
        return null;
    }

}
