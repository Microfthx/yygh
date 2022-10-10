package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.*;
import jw.hospital.yygh.hosp.repository.DepartmentRespository;
import jw.hospital.yygh.hosp.service.DepartmentService;
import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.service.impl
 * @Description:
 * @date 2022/09/13 14:34
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRespository departmentRespository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //paramMap 转换
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString,Department.class);

        Department departmentExist = departmentRespository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if(departmentExist!=null){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRespository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRespository.save(department);
        }
    }

    @Override
    public Page<Department> getDepartmentList(int page, int limit, DepartmentQueryVo departmentQueryVo) {
//        paramMap 转换
//        String paramMapString = JSONObject.toJSONString(paramMap);
//        Department department = JSONObject.parseObject(paramMapString,Department.class);

        Pageable pageable = PageRequest.of(page-1,limit);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase(true)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Department> example = Example.of(department,matcher);
        return departmentRespository.findAll(example,pageable);
    }

    @Override
    public void remove(String hoscode, String depcode) {

        Department department = departmentRespository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            departmentRespository.deleteById(department.getId());
        }
    }

    @Override
    public LinkedList<DepartmentVo> findDeptTree(String hoscode) {
        LinkedList<DepartmentVo> list = new LinkedList<>();
        Department departmentQuery = new Department();
        Example<Department> example = Example.of(departmentQuery);
        List<Department> all = departmentRespository.findAll(example);
        Map<String, List<Department>> departmentMap =
                all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for(Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            String bigCode = entry.getKey();
            List<Department> departmentList = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentList.get(0).getDepname());

            //封装小科室
            List<DepartmentVo> children = new LinkedList<>();
            for (Department department : departmentList) {
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepcode(department.getDepcode());
                departmentVo1.setDepname(department.getDepname());
                children.add(departmentVo1);
            }

            departmentVo.setChildren(children);

            list.add(departmentVo);
        }

        return list;
    }

    @Override
    public String getDepName(String hoscode,String depcode) {
        Department department = departmentRespository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null){
            return department.getDepname();
        }
        return null;
    }
}
