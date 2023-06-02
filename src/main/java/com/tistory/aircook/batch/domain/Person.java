package com.tistory.aircook.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    //성
    private String lastName;
    
    //이름
    private String firstName;

}
