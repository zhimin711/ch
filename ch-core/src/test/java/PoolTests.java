import com.ch.pool.DefaultThreadPool;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 描述：PACKAGE_NAME
 *
 * @author 80002023
 *         2017/3/15.
 * @version 1.0
 * @since 1.8
 */
public class PoolTests {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        Runnable r1 = () -> System.out.println("test1");
        DefaultThreadPool.exe(r1);
        Thread.sleep(1000);
        Callable<String> task = () -> "Callable - String";

        Future<String> future = DefaultThreadPool.submit(task);
        System.out.println(future.get());

        Thread.sleep(1000);

        List<Callable<String>> callables = Lists.newArrayList();
        for (int i = 1; i < 11; i++) {
            int id = i;
            Callable<String> t = () -> "Callable - String" + id;
            callables.add(t);
        }
        String t = DefaultThreadPool.invokeAny(callables);
        System.out.println(t);


        Callable<String> t2 = () -> {
            throw new Exception("Exception");
        };
        callables.add(t2);
        List<Future<String>> futures = DefaultThreadPool.invokeAll(callables);
        boolean ok = false;
        while (futures != null && !ok) {
            for (Future<String> f : futures) {
                System.out.println(f.get() + ":" + f.isDone() + ":" + f.isCancelled());
                if (!f.isDone()) {
                    ok = false;
                    break;
                } else {
                    ok = true;
                }
            }
        }
        Thread.sleep(2000);
        System.out.println("END!");
    }
}
