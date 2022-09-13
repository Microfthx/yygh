package jw.hospital.yygh.hosp.service;


import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface ScheduleService {

    void save(Map<String, Object> paramMap);

}
