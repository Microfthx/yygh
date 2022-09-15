package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import jw.hospital.yygh.cmn.DictFeignClient;
import jw.hospital.yygh.hosp.repository.HospitalRespository;
import jw.hospital.yygh.hosp.repository.ScheduleRespository;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.HospitalSet;
import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private ScheduleRespository scheduleRespository;

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page-1,limit);
        //创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //hospitalQueryVo转换VO对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        Example<Hospital> example = Example.of(hospital,matcher);
        Page<Hospital> pages = hospitalRespository.findAll(example, pageable);
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRespository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRespository.save(hospital);
    }

    @Override
    public Map<String,Object> getHospById(String id) {
        Map<String,Object> resMap = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRespository.findById(id).get());
        resMap.put("hospital",hospital);
        resMap.put("bookingRule",hospital.getBookingRule());

        hospital.setBookingRule(null);
        return resMap;
    }

    @Override
    public Schedule getScheduleDetail(String hoscode, String depcode, String workDate) {
        Schedule schedule = scheduleRespository.getScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, workDate);
        return schedule;
    }

    @Override
    public Map<String,Object> getScheduleByHoscode(String hoscode) {
        Map<String,Object> resMap = new HashMap<>();

        List<Schedule> scheduleList = scheduleRespository.getSchedulesByHoscode(hoscode);
        resMap.put("scheduleList",scheduleList);
        return resMap;
    }

    private Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString);
        return hospital;
    }

}
