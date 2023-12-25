package jw.hospital.yygh.user.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.common.utils.AuthContextHolder;
import jw.hospital.yygh.model.user.UserInfo;
import jw.hospital.yygh.user.service.UserInfoService;
import jw.hospital.yygh.vo.user.LoginVo;
import jw.hospital.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.controller
 * @Description:
 * @date 2022/11/03 17:45
 */
@Api(tags = "用户信息api")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;
    //用户手机号登录接口
    @PostMapping("login")
    @ApiOperation(value = "登录")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo,
                           HttpServletRequest request){
        //传递两个参数,第一个参数用户id,第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();

    }


    //获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
