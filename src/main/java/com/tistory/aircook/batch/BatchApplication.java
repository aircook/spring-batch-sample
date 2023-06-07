package com.tistory.aircook.batch;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.tistory.aircook.batch.repository")
public class BatchApplication {

    public static void main(String[] args) {

        Arrays.stream(args).forEach(s ->
                log.info("args is [{}]", s)
        );

        SpringApplication.run(BatchApplication.class, args);
        //배치일때는 모든 처리후 종료되도 되기에 아래와 같이 해도 됨
        //System.exit(SpringApplication.exit(SpringApplication.run(BatchApplication.class, args)));
    }

}
