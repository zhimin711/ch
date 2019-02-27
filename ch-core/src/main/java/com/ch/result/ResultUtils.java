package com.ch.result;

import com.ch.e.CoreError;
import com.ch.e.CoreException;
import com.ch.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 接口结果封装
 *
 * @author zhimin.ma
 */
public class ResultUtils {

    private final static Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    private ResultUtils() {
    }

    public static <T> Result<T> wrap(Invoker<T> invoker) {
        Result<T> result = new Result<>(Status.FAILED);
        try {
            T record = invoker.invoke();
            List<T> records = Collections.emptyList();
            if (record != null) {
                records = Collections.singletonList(record);
            }
            result.setRows(records);
            result.setStatus(Status.SUCCESS);
        } catch (CoreException e) {
            logger.error(e.getMessage(), e);
            result.newError(e.getError());
            result.setStatus(Status.ERROR);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
            result.setStatus(Status.ERROR);
        }
        return result;
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
        Result<T> result = new Result<>(Status.FAILED);
        try {
            List<T> records = invoker.invoke();
            if (records == null) records = Collections.emptyList();
            result.setStatus(Status.SUCCESS);
            result.setRows(records);
        } catch (CoreException e) {
            logger.error(e.getMessage(), e);
            result.newError(e.getError());
            result.setStatus(Status.ERROR);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
            result.setStatus(Status.ERROR);
        }
        return result;
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
        } catch (CoreException e) {
            logger.error(e.getMessage(), e);
            result.newError(e.getError());
            result.setStatus(Status.ERROR);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
            result.setStatus(Status.ERROR);
        }
        return result;
    }
}
