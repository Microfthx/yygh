package jw.hospital.yygh.hosp.service;

import jw.hospital.yygh.model.hosp.Hospital;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.service
 * @Description:
 * @date 2022/08/25 11:08
 */
@Service
public interface HospitalService {

    void save(Map<String ,Object > paramMap);

    List<Hospital> show();
}
