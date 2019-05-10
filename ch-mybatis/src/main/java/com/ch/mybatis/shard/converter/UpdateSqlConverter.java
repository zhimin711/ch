/**
 *
 */
package com.ch.mybatis.shard.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author Zhimin.Ma
 */
public class UpdateSqlConverter extends AbstractSqlConverter {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.code.shardbatis.converter.AbstractSqlConverter#doConvert(net
     * .sf.jsqlparser.statement.Statement, java.lang.Object, java.lang.String)
     */
    @Override
    protected Statement doConvert(Statement statement, Object params,
                                  String mapperId) {
        if (!(statement instanceof Update)) {
            throw new IllegalArgumentException(
                    "The argument statement must is instance of Update.");
        }
        Update update = (Update) statement;
//        String name = update.getTables().getName();
        update.getTables().forEach(r -> r.setName(this.convertTableName(r.getName(), params, mapperId)));
        return update;
    }

}
