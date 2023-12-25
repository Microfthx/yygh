package jw.hospital.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jw.hospital.yygh.cmn.DictFeignClient;
import jw.hospital.yygh.enums.DictEnum;
import jw.hospital.yygh.model.user.Patient;
import jw.hospital.yygh.user.mapper.PatientMapper;
import jw.hospital.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.service.impl
 * @Description:
 * @date 2022/12/02 15:35
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>  implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllUserId(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);

        patientList.stream().forEach(item -> {
            this.packPatient(item);
        });
        return patientList;
    }

    @Override
    public Patient getPatientId(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    private Patient packPatient(Patient patient) {
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        String contactsCertificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsName());
                String provinceString=dictFeignClient.getName(patient.getProvinceCode());
        String cityString = dictFeignClient.getName(patient.getCityCode());
        String districtString=dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString",certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString",contactsCertificatesTypeString);
        patient.getParam().put("provinceString",provinceString);
        patient.getParam().put("cityString",cityString);
        patient.getParam().put("districtString",districtString);
        patient.getParam().put("fullAddress",provinceString+cityString+districtString+patient.getAddress());
        return patient;
    }
}
