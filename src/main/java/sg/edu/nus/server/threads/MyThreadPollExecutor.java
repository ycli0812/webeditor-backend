package sg.edu.nus.server.threads;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MyThreadPollExecutor extends ThreadPoolTaskExecutor {
    @Override
    public void execute(@NotNull Runnable task) {
        System.out.println("Task execute, current active task number:" + getThreadPoolExecutor().getActiveCount());
        super.execute(task);
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        System.out.println("Runnable task submit, current task number:" + getThreadPoolExecutor().getActiveCount());
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        System.out.println("Callable task execute, current task number:" + getThreadPoolExecutor().getActiveCount());
        return super.submit(task);
    }
}