package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import jw.hospital.yygh.hosp.repository.DepartmentRespository;
import jw.hospital.yygh.hosp.repository.ScheduleRespository;
import jw.hospital.yygh.hosp.service.DepartmentService;
import jw.hospital.yygh.hosp.service.ScheduleService;
import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import jw.hospital.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRespository scheduleRespository;

    @Override
    public void save(Map<String, Object> paramMap){
        //paramMap 转换
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString,Schedule.class);

        Schedule scheduleExist = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if(scheduleExist!=null){
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleRespository.save(scheduleExist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRespository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
//        paramMap 转换
//        String paramMapString = JSONObject.toJSONString(paramMap);
//        Department department = JSONObject.parseObject(paramMapString,Department.class);

        Pageable pageable = PageRequest.of(page-1,limit);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase(true)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Schedule> example = Example.of(schedule,matcher);
        return scheduleRespository.findAll(example,pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {

        Schedule schedule = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule!=null){
            scheduleRespository.deleteById(schedule.getId());
        }
    }
}
