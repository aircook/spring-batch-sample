package com.tistory.aircook.batch.controller;

import com.tistory.aircook.batch.domain.Person;
import com.tistory.aircook.batch.repository.PeopleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 테스트용 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleMapper peopleMapper;
    private final Job userJob;
    private final JobLauncher jobLauncher;

    @GetMapping("/peoples")
    public List<Person> selectPeople() {
        //return List.of(new Person("Ha", "Lee"), new Person("Ja", "Lee"));
        return peopleMapper.selectPeople();
    }

    @GetMapping("/peoples-converted")
    public List<Person> selectPeopleConverted() {
        return peopleMapper.selectPeopleConverted();
    }

    @GetMapping("/execute-batch")
    public String executeBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                //.addString("id", "")
                //.addDate("date", new Date())
                .toJobParameters();

        //batch 실행, 자동실행이 아닌경우 배치를 실행한다.
        jobLauncher.run(userJob, jobParameters);

        // job 실행 시간이 3초일때, 3초가 지나야 아래 값이 리턴된다. Good Job~!!
        return "batch completed";

    }

}
