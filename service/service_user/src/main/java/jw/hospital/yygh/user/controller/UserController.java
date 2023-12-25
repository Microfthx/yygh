package jw.hospital.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.model.user.UserInfo;
import jw.hospital.yygh.user.service.UserInfoService;
import jw.hospital.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.controller
 * @Description:
 * @date 2022/12/05 11:04
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    //用户列表(条件查询带分页)
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       UserInfoQueryVo queryVo){
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> userInfoIPage = userInfoService.selectPage(pageParam,queryVo);
        return Result.ok(userInfoIPage);
    }

    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId,@PathVariable Integer status) {
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId){
        Map<String, Object> map=userInfoService.show(userId);
        return Result.ok(map);
    }

    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,@PathVariable Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }
}
