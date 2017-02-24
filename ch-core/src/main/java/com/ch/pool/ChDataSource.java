package com.ch.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.ch.utils.EncryptUtils;

/**
 * 描述：com.zh.jdbc.pool
 *
 * @author 80002023
 *         2017/2/21.
 * @version 1.0
 * @since 1.8
 */
public class ChDataSource extends DruidDataSource {

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
