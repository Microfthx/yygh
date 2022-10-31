package jw.hospital.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.hosp.service.HospitalSetService;
import jw.hospital.yygh.model.hosp.HospitalSet;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.vo.hosp.HospitalSetQueryVo;
import jw.hospital.yygh.common.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有信息
    @ApiOperation(value = "获取所有医院设置")
    @PostMapping("findAll")
    public Result findAllHospitalSet(){
        //调用service方法
        return Result.ok(hospitalSetService.list());
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("removeHospitalSet/{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return flag?Result.ok():Result.fail();
    }

    @ApiOperation(value = "条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if(hospitalSetQueryVo!=null){
            if(hospitalSetQueryVo.getHoscode()!=null){
                wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
            }
            if(hospitalSetQueryVo.getHosname()!=null){
                wrapper.like("hosname",hospitalSetQueryVo.getHosname());
            }
        }
        return Result.ok(hospitalSetService.page(page,wrapper));
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        Random random = new Random();
        MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000));

        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据IP获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
        //设置状态 1 使用 0 不能使用
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);

        if(flag){
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemoveHospitalSet")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(value = "设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation(value = "发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String key = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
