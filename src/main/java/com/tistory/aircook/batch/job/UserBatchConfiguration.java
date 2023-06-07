package com.tistory.aircook.batch.job;

import com.tistory.aircook.batch.domain.Person;
import com.tistory.aircook.batch.listener.UserJobCompletionNotificationListener;
import com.tistory.aircook.batch.processor.PersonItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * spring batcch 5.0.2을 사용
 */
@Configuration
@Slf4j
public class UserBatchConfiguration {

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Person> userFileReader() {
		log.info("Declare userFileReader bean.");
		return new FlatFileItemReaderBuilder<Person>()
			.name("userFileReader")
			.resource(new ClassPathResource("sample-data.csv"))
			.delimited()
			.names(new String[]{"firstName", "lastName"})
			.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
				setTargetType(Person.class);
			}})
			.build();
	}

	@Bean
	public PersonItemProcessor userProcessor() {
		log.info("Declare processor bean.");
		return new PersonItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Person> userJdbcWriter(DataSource dataSource) {
		log.info("Declare userJdbcWriter bean.");
		return new JdbcBatchItemWriterBuilder<Person>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
			.dataSource(dataSource)
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job userJob(JobRepository jobRepository,
					   UserJobCompletionNotificationListener listener, Step userStep) {
		log.info("Declare userJob bean.");
		return new JobBuilder("userJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			//.flow(step1) // Job: [FlowJob: [name=importUserJob]] launched with the following parameters: [{'run.id':'{value=1, type=class java.lang.Long, identifying=true}'}]
			//.end()
			.start(userStep) // Job: [SimpleJob: [name=importUserJob]] launched with the following parameters: [{'run.id':'{value=1, type=class java.lang.Long, identifying=true}'}]
			.build();
	}

	@Bean
	public Step userStep(JobRepository jobRepository,
			PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Person> userJdbcWriter) {
		log.info("Declare userStep bean.");
		return new StepBuilder("userStep", jobRepository)
			.<Person, Person> chunk(10, transactionManager)
			.reader(userFileReader())
			.processor(userProcessor())
			.writer(userJdbcWriter)
			.build();
	}
	// end::jobstep[]
}
