package com.gegaojian.girl.service;

import com.gegaojian.girl.domain.Girl;
import com.gegaojian.girl.domain.Result;
import com.gegaojian.girl.exception.ExceptionCodeEnum;
import com.gegaojian.girl.exception.GirlException;
import com.gegaojian.girl.repository.GirlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GirlService {
    @Autowired
    private GirlRepository girlRepository;

    @Transactional
    public void addTwo(){
        Girl girlA = new Girl();
        girlA.setCupSize("A");
        girlA.setAge(18);
        girlRepository.save(girlA);

        Girl girlB = new Girl();
        girlB.setCupSize("B");
        girlB.setAge(18);
        girlRepository.save(girlB);
    }

    public Girl getAge(Integer id) throws GirlException{
        Girl girl = girlRepository.findById(id).get();
        Integer age = girl.getAge();
        if (age < 10){
            throw new GirlException(ExceptionCodeEnum.PRIMARY_SCHOOL);
        }else if (age < 16){
            throw new GirlException(ExceptionCodeEnum.MIDDLE_SCHOOL);
        }
        return girlRepository.findById(id).get();
    }
}
