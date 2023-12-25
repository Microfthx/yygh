package jw.hospital.yygh.hosp.service;

import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import jw.hospital.yygh.vo.hosp.DepartmentVo;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Service
public interface DepartmentService {
    void save(Map<String, Object> paramMap);


    Page<Department> getDepartmentList(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode,String depcode);

    Department getDepartment(String hoscode, String depcode);
}
