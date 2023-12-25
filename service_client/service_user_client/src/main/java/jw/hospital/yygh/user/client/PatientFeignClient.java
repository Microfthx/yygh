package jw.hospital.yygh.user.client;

import jw.hospital.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.cmn
 * @Description:
 * @date 2022/09/14 15:13
 */
@FeignClient("service-user")
@Repository
public interface PatientFeignClient {

    @GetMapping("/api/user/patient/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id);
}
