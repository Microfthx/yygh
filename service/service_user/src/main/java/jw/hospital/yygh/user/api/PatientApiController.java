package jw.hospital.yygh.user.api;

import io.swagger.annotations.ApiParam;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.common.utils.AuthContextHolder;
import jw.hospital.yygh.model.user.Patient;
import jw.hospital.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.api
 * @Description:
 * @date 2022/12/02 15:34
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;
    //获取就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> patientList=patientService.findAllUserId(userId);
        return Result.ok(patientList);
    }
    //添加就诊人
    @PostMapping("auth/savePatient")
    private Result savePatient(@RequestBody Patient patient, HttpServletRequest request){
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }
    //根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id){
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
    //修改就诊人
    @PostMapping("auth/updatePatient")
    private Result updatePatient(@RequestBody Patient patient){
        //获取当前登录用户id
        patientService.updateById(patient);
        return Result.ok();
    }
    //删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id){
        patientService.removeById(id);
        return Result.ok();
    }
    //获取就诊人
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(
            @ApiParam(name = "id",value = "就诊人 id",required = true)
            @PathVariable("id") Long id){
        return patientService.getPatientId(id);
    }
}
