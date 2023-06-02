package com.tistory.aircook.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class BatchApplication {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        //SpringApplication.run(BatchApplication.class, args);
        //applicationContext = SpringApplication.run(BatchApplication.class, args);
        System.exit(SpringApplication.exit(SpringApplication.run(BatchApplication.class, args)));
        //displayAllBeans();
    }

    public static void displayAllBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : beanNames) {
            log.debug("{}", beanName);
        }
    }

}
