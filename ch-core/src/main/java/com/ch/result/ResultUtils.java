package com.ch.result;

import com.ch.e.CoreError;
import com.ch.e.CoreException;
import com.ch.type.Status;
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

    public static <T> BaseResult<T> wrap(Invoker<T> invoker) {
        BaseResult<T> result = new BaseResult<>(Status.ERROR);
        try {
            T record = invoker.invoke();
            List<T> records = Collections.emptyList();
            if (record != null) {
                records = Collections.singletonList(record);
            }
            result.setStatus(Status.SUCCESS);
            result.setRows(records);
        } catch (CoreException e) {
            logger.error(e.getMessage(), e);
            result.newError(e.getError());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
        }
        return result;
    }

    public static <T> BaseResult<T> wrapList(InvokerList<T> invoker) {
        BaseResult<T> result = new BaseResult<>(Status.ERROR);
        try {
            List<T> records = invoker.invoke();
            if (records == null) records = Collections.emptyList();
            result.setStatus(Status.SUCCESS);
            result.setRows(records);
        } catch (CoreException e) {
            logger.error(e.getMessage(), e);
            result.newError(e.getError());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.newError(CoreError.UNKNOWN);
        }
        return result;
    }
}
