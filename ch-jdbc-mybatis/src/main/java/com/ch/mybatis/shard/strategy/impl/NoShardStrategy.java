/**
 *
 */
package com.ch.mybatis.shard.strategy.impl;


import com.ch.mybatis.shard.strategy.ShardStrategy;

/**
 * 不进行分表的策略，供测试用
 *
 * @author Zhimin.Ma
 */
public class NoShardStrategy implements ShardStrategy {

    /* (non-Javadoc)
     * @see com.google.code.shardbatis.strategy.ShardStrategy#getTargetTableName(java.lang.String, java.lang.Object, java.lang.String)
     */
    public String getTargetTableName(String baseTableName, Object params,
                                     String mapperId) {
        return baseTableName;
    }

}
