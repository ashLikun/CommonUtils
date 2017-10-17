package com.ashlikun.utils.encryption;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/10/17　14:25
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

import java.security.Provider;

/**
 * Implementation of Provider for SecureRandom. The implementation     supports the
 * "SHA1PRNG" algorithm described in JavaTM Cryptography Architecture, API
 * Specification & Reference
 */
public final class CryptoProvider extends Provider {
    /**
     * Creates a Provider and puts parameters
     */
    public CryptoProvider() {
        super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
        put("SecureRandom.SHA1PRNG",
                "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
    }
}
