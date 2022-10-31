package jw.hospital.yygh.hosp.controller;

import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.hosp.service.DepartmentService;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import jw.hospital.yygh.vo.hosp.DepartmentVo;
import jw.hospital.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.controller
 * @Description:
 * @date 2022/09/14 14:32
 */
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //医院列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}
