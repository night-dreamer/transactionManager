package com.beiji.transactionmanager;

import com.beiji.transactionmanager.model.Transaction;
import com.beiji.transactionmanager.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionServiceStressTest {

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService.getTransactions().clear();
    }

    @Test
    public void testConcurrentCreates() throws InterruptedException {
        int threadCount = 200; // 增加线程数到200
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    Transaction transaction = new Transaction();
                    transaction.setId("test-" + index);
                    transaction.setAccountId("acc" + index);
                    transaction.setType(index % 2 == 0 ? "deposit" : "withdrawal");
                    transaction.setAmount(100.0 + index);
                    transaction.setStatus(index % 2 == 0 ? "success" : "failure");
                    transactionService.createTransaction(transaction);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int totalCount = transactionService.getTotalCount();
        assertEquals(threadCount, totalCount);

        System.out.println("=== 并发创建测试结果 ===");
        System.out.println("线程数: " + threadCount);
        System.out.println("成功创建交易数: " + totalCount);
        System.out.println("耗时: " + duration + " 毫秒");
        System.out.println("每秒创建交易数: " + (threadCount / (duration / 1000.0)));
        executor.shutdown();
    }

    @Test
    public void testConcurrentUpdates() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 预先创建100个交易
        for (int i = 0; i < threadCount; i++) {
            Transaction t = new Transaction();
            t.setId("update-" + i);
            t.setAccountId("acc" + i);
            t.setType("deposit");
            t.setAmount(100.0 + i);
            t.setStatus("success");
            transactionService.createTransaction(t);
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    Transaction transaction = new Transaction();
                    transaction.setId("update-" + index);
                    transaction.setAccountId("acc" + index + "-updated");
                    transaction.setType("withdrawal");
                    transaction.setAmount(200.0 + index);
                    transaction.setStatus("failure");
                    transactionService.updateTransaction(transaction);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int totalCount = transactionService.getTotalCount();
        assertEquals(threadCount, totalCount);

        System.out.println("=== 并发更新测试结果 ===");
        System.out.println("线程数: " + threadCount);
        System.out.println("成功更新交易数: " + totalCount);
        System.out.println("耗时: " + duration + " 毫秒");
        System.out.println("每秒更新交易数: " + (threadCount / (duration / 1000.0)));
        executor.shutdown();
    }

    @Test
    public void testConcurrentDeletes() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 预先创建100个交易
        for (int i = 0; i < threadCount; i++) {
            Transaction t = new Transaction();
            t.setId("delete-" + i);
            t.setAccountId("acc" + i);
            t.setType("deposit");
            t.setAmount(100.0 + i);
            t.setStatus("success");
            transactionService.createTransaction(t);
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    transactionService.deleteTransaction("delete-" + index);
                } catch (IllegalArgumentException e) {
                    // 忽略异常，记录日志或处理
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int totalCount = transactionService.getTotalCount();
        assertEquals(0, totalCount);

        System.out.println("=== 并发删除测试结果 ===");
        System.out.println("线程数: " + threadCount);
        System.out.println("成功删除交易数: " + (100 - totalCount));
        System.out.println("耗时: " + duration + " 毫秒");
        System.out.println("每秒删除交易数: " + ((100 - totalCount) / (duration / 1000.0)));
        executor.shutdown();
    }
}