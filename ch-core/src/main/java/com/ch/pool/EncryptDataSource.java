package com.ch.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.ch.utils.EncryptUtils;

/**
 * 描述:加密数据数据源
 * <p>support spring xml</p>
 *
 * @author 80002023
 * @version 1.0
 * @see EncryptUtils#decrypt(String, String)
 * @since 1.8
 */
public class EncryptDataSource extends DruidDataSource {

    private String pubKey;

    @Override
    public void setUsername(String username) {
        //
        super.setUsername(EncryptUtils.decrypt(pubKey, username));
    }

    @Override
    public void setPassword(String password) {
        //
        super.setPassword(EncryptUtils.decrypt(pubKey, password));
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}
