package com.tistory.aircook.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@Slf4j
//@MapperScan(basePackages = "com.tistory.aircook.batch.repository")
/**
 * MyBatis에 대한 세세한 설정이 필요하면 정의
 * 현재는 application.yml 를 통해 정의했음
 */
public class MyBatisConfiguration {
}
