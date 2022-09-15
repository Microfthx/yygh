package jw.hospital.yygh.hosp.repository;

import jw.hospital.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRespository extends MongoRepository<Schedule,String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode,String scheduleId);

    Schedule getScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, String workDate);

    List<Schedule> getSchedulesByHoscode(String hoscode);
}
