package jw.hospital.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jw.hospital.yygh.model.user.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService extends IService<Patient> {
    List<Patient> findAllUserId(Long userId);

    Patient getPatientId(Long id);
}
