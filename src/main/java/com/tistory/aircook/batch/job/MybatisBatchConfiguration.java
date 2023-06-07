package com.tistory.aircook.batch.job;

import com.tistory.aircook.batch.domain.Person;
import com.tistory.aircook.batch.listener.MybatisJobCompletionNotificationListener;
import com.tistory.aircook.batch.processor.PersonItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * MyBatisReader, MyBatisWriter를 통한 배치 설정, 다음 DI 조건에 유의하여 이름 부여할 것
 * Spring - Dependency injection in @Bean method parameters
 * https://www.logicbig.com/tutorials/spring-framework/spring-core/javaconfig-methods-inter-dependency.html
 * 1. by type
 * 2. by name
 * 3. by name to matching qualifier
 * 4. by qualifiers
 */
@Configuration
@Slf4j
public class MybatisBatchConfiguration {

    @Bean
    public MyBatisPagingItemReader<Person> myBatisReader(SqlSessionFactory sqlSessionFactory) {
        log.info("Declare myBatisReader bean.");
        return new MyBatisPagingItemReaderBuilder<Person>()
                .pageSize(3) //paging size~!, 3개씩 가져옴
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.tistory.aircook.batch.repository.PeopleMapper.selectPagingPeople")
                .build();
    }

    @Bean
    public PersonItemProcessor myBatisProcessor() {
        log.info("Declare myBatisProcessor bean.");
        return new PersonItemProcessor();
    }

    /**
     * 대문자로 변경한후 PEOPLE --> PEOPLE_CONVERTED
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public MyBatisBatchItemWriter<Person> myBatisWriter(SqlSessionFactory sqlSessionFactory) {
        log.info("Declare myBatisWriter bean.");
        return new MyBatisBatchItemWriterBuilder<Person>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.tistory.aircook.batch.repository.PeopleMapper.insertPeopleConverted")
                .build();
    }

    @Bean
    public Job myBatisJob(JobRepository jobRepository,
                          MybatisJobCompletionNotificationListener listener, Step myBatisStep) {
        log.info("Declare myBatisJob bean.");
        return new JobBuilder("myBatisJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(myBatisStep)
                .build();
    }

    @Bean
    public Step myBatisStep(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      MyBatisPagingItemReader<Person> myBatisReader,
                      MyBatisBatchItemWriter<Person> myBatisWriter) {
        log.info("Declare myBatisStep bean.");
        return new StepBuilder("myBatisStep", jobRepository)
                .<Person, Person>chunk(5, transactionManager) //transaction의 단위, 5개씩 processing & writing
                .reader(myBatisReader)
                .processor(myBatisProcessor())
                .writer(myBatisWriter)
                .build();
    }
}
