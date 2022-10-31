package jw.hospital.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.hosp.service.ScheduleService;
import jw.hospital.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.controller
 * @Description:
 * @date 2022/10/04 0:20
 */
@Api(tags = "排班管理")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号和科室编号,查询排版数据
    @ApiOperation(value = "获取所有排班规则")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);

        return Result.ok(map);
    }

    //根据医院编号、科室编号和工作日期,查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                  @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getRuleScheduleDetail(hoscode,depcode,workDate);

        return Result.ok(list);
    }
}
