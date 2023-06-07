package com.tistory.aircook.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
//@Component
@RequiredArgsConstructor
public class BatchApplicationRunner implements ApplicationRunner {

    private final DataSource dataSource;

    private final ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Connection connection = dataSource.getConnection();
        log.info("Catalog is {} ", connection.getCatalog());
        log.info("Schema is {} ", connection.getSchema());
        log.info("ClientInfo is {} ", connection.getClientInfo());
        log.info("Url is {} ", connection.getMetaData().getURL());
        log.info("DriverName is {} ", connection.getMetaData().getDriverName());
        log.info("UserName is {} ", connection.getMetaData().getUserName());
        log.info("CatalogSeparator is {} ", connection.getMetaData().getCatalogSeparator());
        log.info("DatabaseProductName is {} ", connection.getMetaData().getDatabaseProductName());
        log.info("DatabaseProductVersion is {} ", connection.getMetaData().getDatabaseProductVersion());

        displayAllBeans();

    }

    public void displayAllBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        int i = 0;
        for(String beanName : beanNames) {
            log.debug("[{}] {}", i++, beanName);
        }
    }
}
