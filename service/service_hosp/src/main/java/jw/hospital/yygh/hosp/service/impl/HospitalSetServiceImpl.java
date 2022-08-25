package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jw.hospital.yygh.hosp.mapper.HospitalSetMapper;
import jw.hospital.yygh.hosp.repository.HospitalRespository;
import jw.hospital.yygh.hosp.service.HospitalSetService;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.Map;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Override
    public String getSignKey(String hosCode) {
        QueryWrapper<HospitalSet> query = new QueryWrapper<>();
        query.eq("hoscode",hosCode);
        String signKey = baseMapper.selectOne(query).getSignKey();
        return signKey;
    }
}
