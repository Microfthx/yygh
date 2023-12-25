package jw.hospital.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import jw.hospital.yygh.common.helper.JwtHelper;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.model.user.UserInfo;
import jw.hospital.yygh.user.service.UserInfoService;
import jw.hospital.yygh.user.utils.ConstantWxPropertiesUtils;
import jw.hospital.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.user.api
 * @Description:
 * @date 2022/11/30 14:07
 */
//微信操作接口
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;
    //1 生成微信扫描二维码
    //返回二维码所需要的参数
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
            map.put("scope","snsapi_login");
            String wxOpenRedirectUrl = ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirect_uri",wxOpenRedirectUrl);
            map.put("state",System.currentTimeMillis()+"");
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }
    //2 回调的方法
    @GetMapping("callback")
    public String callback(String code,String state){
        System.out.println("code:"+code);
        //使用code和appid以及appscrect换取access token+
        StringBuffer baseAccessTokenUrl=new StringBuffer()
        .append("https://api.weixin.qq.com/sns/oauth2/access_token")
        .append("?appid=%s")
        .append("&secret=%s")
        .append("&code=%s")
        . append ("&grant_type=authorization_code") ;
        String accessTokenUrl = String. format(baseAccessTokenUrl. toString(),
            ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
            code);
        try{
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo"+
                    "?access_token=%s"+
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
            String resultInfo = HttpClientUtils.get(userInfoUrl);
            JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);

            String nickname = resultUserInfoJson.getString("nickname");
            String headimgurl = resultUserInfoJson.getString("headimgurl");

            //判断数据库是否存在微信的扫描人信息
            //根据openid判断
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            if(userInfo == null){
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }

            //返回name和token字符串
            Map<String,String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //判断userInfo是否有手机号，如果手机号为空，返回openid
            //如果手机号不为空，返回openid值是空字符串
            //前端判断：如果openid不为空，绑定手机号，如果openid为空，不需要绑定手机号
            if(StringUtils.isEmpty(userInfo.getPhone())){
                map.put("openid",userInfo.getOpenid());
            } else {
                map.put("openid","");
            }
            //使用jwt生成token字符串
            String token = JwtHelper.createToken(userInfo.getId(),name);
            map.put("token",token);

            return "redirect:"+ ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token=" +map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode(map.get("name"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
