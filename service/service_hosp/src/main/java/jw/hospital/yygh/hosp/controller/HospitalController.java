package jw.hospital.yygh.hosp.controller;

import io.swagger.annotations.Api;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.controller
 * @Description:
 * @date 2022/09/14 14:32
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //医院列表
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospPage( page,  limit,  hospitalQueryVo);
        return Result.ok(pageModel);
    }


}
