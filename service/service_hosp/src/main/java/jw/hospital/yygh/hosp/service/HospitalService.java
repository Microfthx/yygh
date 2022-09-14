package jw.hospital.yygh.hosp.service;

import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;
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

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
