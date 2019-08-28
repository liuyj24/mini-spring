package com.shenghao.controller;

import com.shenghao.beans.AutoWired;
import com.shenghao.service.SalaryService;
import com.shenghao.web.mvc.Controller;
import com.shenghao.web.mvc.RequestMapping;
import com.shenghao.web.mvc.RequestParam;

@Controller
public class SalaryController {

    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("/getSalary")
    public Integer getSalary(@RequestParam("name")String name,
                             @RequestParam("experience")String experience){
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
