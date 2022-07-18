package jw.hospital.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.hosp.service.HospitalSetService;
import jw.hospital.yygh.model.hosp.HospitalSet;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        //调用service方法
        return Result.ok(hospitalSetService.list());
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return flag?Result.ok():Result.fail();
    }

    @ApiOperation(value = "条件查询带分页")
    @GetMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if(hospitalSetQueryVo.getHoscode()!=null){
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        if(hospitalSetQueryVo.getHosname()!=null){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        return Result.ok(hospitalSetService.page(page,wrapper));
    }
}
