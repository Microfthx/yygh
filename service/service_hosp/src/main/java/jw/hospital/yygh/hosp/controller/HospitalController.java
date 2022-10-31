package jw.hospital.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.common.helper.HttpRequestHelper;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.common.utils.MD5;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.Schedule;
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
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //医院列表
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospPage( page,  limit,  hospitalQueryVo);
        List<Hospital> content = pageModel.getContent();
        long totalElements = pageModel.getTotalElements();
        return Result.ok(pageModel);
    }

    //医院列表
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //医院详情信息
    @ApiOperation(value = "医院详情信息")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String,Object> hospital = hospitalService.getHospById(id);
        return Result.ok(hospital);
    }

    //排班详情信息
    @ApiOperation(value = "排班详情信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        Schedule schedule = hospitalService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(schedule);
    }
    //排班信息
    @ApiOperation(value = "排班信息")
    @GetMapping("getScheduleByHoscode/{hoscode}")
    public Result getScheduleByHoscode(@PathVariable String hoscode){
        Map<String,Object> scheduleList= hospitalService.getScheduleByHoscode(hoscode);
        return Result.ok(scheduleList);
    }
}
