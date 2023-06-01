package com.tistory.aircook.batch.job;

import com.tistory.aircook.batch.domain.Person;
import com.tistory.aircook.batch.listener.JobCompletionNotificationListener;
import com.tistory.aircook.batch.processor.PersonItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
public class BatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Person> reader() {

		log.info("reader 빈으로 선언했음..");

		return new FlatFileItemReaderBuilder<Person>()
			.name("personItemReader")
			.resource(new ClassPathResource("sample-data.csv"))
			.delimited()
			.names(new String[]{"firstName", "lastName"})
			.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
				setTargetType(Person.class);
			}})
			.build();
	}

	@Bean
	public PersonItemProcessor processor() {

		log.info("processor 빈으로 선언했음..");

		return new PersonItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {

		log.info("writer 빈으로 선언했음..");

		return new JdbcBatchItemWriterBuilder<Person>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
			.dataSource(dataSource)
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobRepository jobRepository,
							 JobCompletionNotificationListener listener, Step step1) {

		log.info("job 을 빈으로 선언했음..");

		return new JobBuilder("importUserJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.end()
			//	.start(step1) // Job: [SimpleJob: [name=importUserJob]] launched with the following parameters: [{'run.id':'{value=1, type=class java.lang.Long, identifying=true}'}]
				.build();
	}

	@Bean
	public Step step1(JobRepository jobRepository,
			PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Person> writer) {

		log.info("step1 을 빈으로 선언했음..");
		
		return new StepBuilder("step1", jobRepository)
			.<Person, Person> chunk(10, transactionManager)
			.reader(reader())
			.processor(processor())
			.writer(writer)
			.build();
	}
	// end::jobstep[]
}
