package jw.hospital.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jw.hospital.yygh.model.user.Patient;
import jw.hospital.yygh.user.service.PatientService;
import org.springframework.util.StringUtils;
import jw.hospital.yygh.common.exception.HospitalException;
import jw.hospital.yygh.common.helper.JwtHelper;
import jw.hospital.yygh.common.result.ResultCodeEnum;
import jw.hospital.yygh.enums.AuthStatusEnum;
import jw.hospital.yygh.model.user.UserInfo;
import jw.hospital.yygh.user.mapper.UserInfoMapper;
import jw.hospital.yygh.user.service.UserInfoService;
import jw.hospital.yygh.vo.user.LoginVo;
import jw.hospital.yygh.vo.user.UserAuthVo;
import jw.hospital.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.service.impl
 * @Description:
 * @date 2022/11/03 17:49
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService{

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private PatientService patientService;

    @Override
    public Map<String, Object> loginUser(LoginVo loginVo){
        //从loginVo获取输入的手机号 和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //判断手机号和验证码是否为空
        if(StringUtils.isEmpty(phone) ||StringUtils.isEmpty(code)){
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        //判断验证码是否一致
        String redisCode = redisTemplate.opsForValue().get("19921535102");
        if(!code.equals(redisCode)){
            throw new HospitalException(ResultCodeEnum.CODE_ERROR);
        }

        //如果是微信扫码登录，则Openid有值，则绑定手机号码，执行后userInfo就!=null了不会走68行的手机号登录
        UserInfo userInfo = null;
        if(!org.springframework.util.StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new HospitalException(ResultCodeEnum.DATA_ERROR);
            }
        }
        //如果userinfo为空，进行正常手机登录
        if(userInfo == null) {
            //判断是否第一次登录：根据手机号查询数据库，如果不存在相同手机号就是第一次登录
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone",phone);
            userInfo = baseMapper.selectOne(wrapper);
            //如果userInfo不为null，则不执行if里面，直接去93行执行代码
            if(userInfo == null) { //第一次使用这个手机号登录
                //添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }

        //校验是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new HospitalException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //不是第一次登录，直接登录。假设用户是userInfo == null手机号登录的，并且已经注册过不用走73行的代码了，直接走下面的即可。
        //返回登录信息
        //返回登录用户名
        //返回token信息，token信息是用来返回给前台的，执行操作时判断用户是否登录状态，可以设置过期时间用session一样
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        //如果这个用户登录后没有去设置真实姓名，则name为空，那我们就设置该用户它在前端显示的名字为昵称
        if(org.springframework.util.StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        //如果这个用户登录后也没有设置昵称，则name还是空，那我们就设置该用户它在前端显示的名字为它的手机号
        if(org.springframework.util.StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name",name);
        //TODO token生成
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token",token);
        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        return baseMapper.selectOne(queryWrapper);
    }

    //用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        //设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行信息更新
        baseMapper.updateById(userInfo);

    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name=userInfoQueryVo.getKeyword();//用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus();//认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();//开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();//结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
             wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)){
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }
        IPage<UserInfo> userInfoPage = baseMapper.selectPage(pageParam, wrapper);
        //编号对应值
        userInfoPage.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return userInfoPage;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        Map<String, Object> map = new HashMap<>();
        //用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        //就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList",patientList);

        return map;
    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus == 2|| authStatus == -1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    private UserInfo packageUserInfo(UserInfo item) {
        //处理认证状态编码
        item.getParam().put("authStatus",AuthStatusEnum.getStatusNameByStatus(item.getAuthStatus()));
        //处理用户状态 0 1
        String statusString = item.getStatus().intValue()==0?"锁定":"正常";
        item.getParam().put("statusString",statusString);
        return item;
    }

}

