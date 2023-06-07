package com.tistory.aircook.batch.repository;

import com.tistory.aircook.batch.domain.Person;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface PeopleMapper {

    List<Person> selectPeople();
    List<Person> selectPagingPeople();
    List<Person> selectPeopleConverted();
    int insertPeopleConverted();


}
