package com.shenghao.service;

import com.shenghao.beans.Bean;

@Bean
public class SalaryService {
    public Integer calSalary(Integer experience){
        return experience * 5000;
    }
}
