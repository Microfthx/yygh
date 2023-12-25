package jw.hospital.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jw.hospital.yygh.model.user.UserInfo;
import jw.hospital.yygh.vo.user.LoginVo;
import jw.hospital.yygh.vo.user.UserAuthVo;
import jw.hospital.yygh.vo.user.UserInfoQueryVo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> loginUser(LoginVo loginVo);

    UserInfo selectWxInfoOpenId(String openid);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
