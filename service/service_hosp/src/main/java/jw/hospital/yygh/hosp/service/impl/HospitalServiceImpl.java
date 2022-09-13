package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import jw.hospital.yygh.hosp.repository.HospitalRespository;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.service.impl
 * @Description:
 * @date 2022/08/25 11:09
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRespository hospitalRespository;

    @Override
    public void save(Map<String, Object> paramMap){
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRespository.getHospitalByHoscode(hoscode);

        if(hospitalExist != null){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(hospitalExist.getUpdateTime());
            hospital.setIsDeleted(0);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRespository.save(hospital);
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRespository.getHospitalByHoscode(hoscode);
    }

}
