package com.tistory.aircook.batch.listener;

import com.tistory.aircook.batch.domain.Person;
import com.tistory.aircook.batch.repository.PeopleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MybatisJobCompletionNotificationListener implements JobExecutionListener {

    private final PeopleMapper peopleMapper;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! MYBATIS JOB FINISHED! Time to verify the results");
            //결과 조회
            List<Person> list = peopleMapper.selectPeopleConverted();
            log.debug("Results List size is [{}]", list.size());
            list.forEach(
                    person -> log.info("Found [{}] in the database.", person)
            );
        }
    }
}
