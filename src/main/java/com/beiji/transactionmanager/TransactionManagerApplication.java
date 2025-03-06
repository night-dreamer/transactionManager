package com.beiji.transactionmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TransactionManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionManagerApplication.class, args);
    }
}