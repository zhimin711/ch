package com.ch.result;

import com.ch.Status;
import com.ch.e.PubError;
import com.ch.e.PubException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 接口结果封装
 *
 * @author zhimin.ma
 */
@Slf4j
public class ResultUtils {

    private ResultUtils() {
    }

    public static <T> Result<T> wrap(Invoker<T> invoker) {
        try {
            T record = invoker.invoke();
            List<T> records = Collections.emptyList();
            if (record != null) {
                records = Collections.singletonList(record);
            }
            return Result.success(records);
        } catch (PubException e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getError());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(PubError.UNKNOWN);
        }
    }

    /**
     * 装配结果（如结果为 0或为空 则为失败）
     *
     * @param invoker 执行接口
     * @param <T>     执行对象
     * @return 返回Result结果
     */
    public static <T> Result<T> wrapFail(Invoker<T> invoker) {
        Result<T> result = wrap(invoker);
        if (!result.isSuccess()) {
            return result;
        }
        T record = result.get();
        Number numZero = 0;
        if (result.isEmpty()) {
            result.setStatus(Status.FAILED);
        } else if (record instanceof Number && ((Number) record).floatValue() == numZero.floatValue()) {
            result.setStatus(Status.FAILED);
        }
        return result;
    }

    public static <T> Result<T> wrapList(InvokerList<T> invoker) {
        try {
            List<T> records = invoker.invoke();
            if (records == null) records = Collections.emptyList();
            return Result.success(records);
        } catch (PubException e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getError());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(PubError.UNKNOWN);
        }
    }


    public static <T> PageResult<T> wrapPage(InvokerPage<T> invoker) {
        PageResult<T> result = new PageResult<>();
        try {
            InvokerPage.Page<T> page = invoker.invoke();
            if (page == null) {
                result.setRows(Collections.emptyList());
            } else {
                Collection<T> records = page.getRows();
                if (records == null) records = Collections.emptyList();
                result.setRows(records);
                result.setTotal(page.getTotal());
            }
            result.setStatus(Status.SUCCESS);
        } catch (PubException e) {
            log.error(e.getMessage(), e);
            return PageResult.error(e.getError());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return PageResult.error(PubError.UNKNOWN);
        }
        return result;
    }
}
