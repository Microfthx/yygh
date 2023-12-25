package jw.hospital.yygh.hosp.controller.api;

import io.swagger.annotations.Api;
import jw.hospital.yygh.common.helper.HttpRequestHelper;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.common.utils.MD5;
import jw.hospital.yygh.hosp.service.DepartmentService;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.hosp.service.HospitalSetService;
import jw.hospital.yygh.hosp.service.ScheduleService;
import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import jw.hospital.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.hosp.controller.api
 * @Description:
 * @date 2022/08/25 14:05
 */
@Api(tags = "医院api")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //获取签名
        String sign = (String) paramMap.get("sign");

        String hosCode = (String) paramMap.get("hoscode");

        String signKey = hospitalSetService.getSignKey(hosCode);

        String signKeyMd5 = MD5.encrypt(signKey);
//        if(!sign.equals(signKeyMd5)){
////            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
//            return Result.fail();
//        }
        scheduleService.save(paramMap);
        return Result.ok();

    }
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        int limit = Integer.parseInt(map.get("limit").toString());
        int page = Integer.parseInt(map.get("page").toString());

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(map.get("hoscode").toString());
        Page<Schedule> schedulePage = scheduleService.findSchedule(page,limit,scheduleQueryVo);

        return Result.ok(schedulePage);

    }

    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //获取签名
        String sign = (String) paramMap.get("sign");

        String hosCode = (String) paramMap.get("hoscode");

        String signKey = hospitalSetService.getSignKey(hosCode);

        String signKeyMd5 = MD5.encrypt(signKey);
        if(!sign.equals(signKeyMd5)){
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
            return Result.fail();
        }
        departmentService.save(paramMap);
        return Result.ok();

    }

    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取签名
        String sign = (String) paramMap.get("sign");

        String hosCode = (String) paramMap.get("hoscode");

        String signKey = hospitalSetService.getSignKey(hosCode);

        String signKeyMd5 = MD5.encrypt(signKey);
        if(!sign.equals(signKeyMd5)){
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
            return Result.fail();
        }
        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);
        //调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }


    @PostMapping("hospital/show")
    public Result show(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) map.get("hoscode");

        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        String sign = (String) map.get("sign_key");
        if(!sign.equals(signKeyMd5)){
//            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
            return Result.fail();
        }

        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @PostMapping("department/list")
    public Result list(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        int limit = Integer.parseInt(map.get("limit").toString());
        int page = Integer.parseInt(map.get("page").toString());

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(map.get("hoscode").toString());
        Page<Department> departmentList = departmentService.getDepartmentList(page,limit,departmentQueryVo);

        return Result.ok(departmentList);
    }

    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        departmentService.remove(hoscode,depcode);

        return Result.ok();
    }
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode,hosScheduleId);

        return Result.ok();
    }
}
