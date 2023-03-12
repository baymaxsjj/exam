package com.baymax.exam.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/12 19:45
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@RestController
@Tag( name = "获取RSA公钥接口")
@RequestMapping("/rsa")
public class KeyPairController {

    @Autowired
    private KeyPair keyPair;


    @GetMapping("/publicKey")
    public Map<String, Object> getKey() {
        log.info("获取jwt Key");
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
