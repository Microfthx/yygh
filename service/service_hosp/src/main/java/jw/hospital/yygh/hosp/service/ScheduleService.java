package jw.hospital.yygh.hosp.service;


import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface ScheduleService {

    void save(Map<String, Object> paramMap);

    Page<Schedule> findSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getRuleScheduleDetail(String hoscode, String depcode, String workDate);
}
