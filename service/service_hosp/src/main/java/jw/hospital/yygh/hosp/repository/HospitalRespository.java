package jw.hospital.yygh.hosp.repository;

import jw.hospital.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRespository extends MongoRepository<Hospital,String> {
    Hospital getHospitalByHoscode(String hoscode);
}
