package jw.hospital.yygh.hosp.repository;

import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.model.hosp.Hospital;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRespository extends MongoRepository<Department,String> {
    Hospital getHospitalByHoscode(String hoscode);

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
