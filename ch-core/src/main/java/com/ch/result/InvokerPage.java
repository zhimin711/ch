package com.ch.result;

import java.util.Collection;

/**
 * 分页结果封装
 *
 * @author 01370603
 */
@FunctionalInterface
public interface InvokerPage<T> {

    /**
     * 分页结果类
     *
     * @param <T>
     */
    class Page<T> {
        private long total = 0;
        private Collection<T> rows;

        public Page() {
        }

        public Page(long total, Collection<T> rows) {
            this.total = total;
            this.rows = rows;
        }

        public static <T> Page<T> build() {
            return new Page<>();
        }

        public static <T> Page<T> build(long total, Collection<T> rows) {
            return new Page<>(total, rows);
        }

        public long getTotal() {
            return total;
        }

        public Collection<T> getRows() {
            return rows;
        }

        public void setRows(Collection<T> rows) {
            this.rows = rows;
        }
    }

    Page<T> invoke() throws Exception;

}

