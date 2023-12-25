package jw.hospital.yygh.msm.service.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import darabonba.core.client.ClientConfiguration;
import darabonba.core.client.ClientOverrideConfiguration;
import jw.hospital.yygh.msm.service.MsmService;
import jw.hospital.yygh.msm.utils.ConstantPropertiesUtils;
import org.ini4j.Config;
import org.springframework.stereotype.Service;
import com.aliyun.sdk.service.dysmsapi20170525.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.aliyun.sdk.service.dysmsapi20170525.models.*;
/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.msm.service.impl
 * @Description:
 * @date 2022/11/14 17:54
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) throws Exception {
        if(StringUtils.isNullOrEmpty(phone)){
            return false;
        }
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ConstantPropertiesUtils.ACCESS_KEY_ID)
                .accessKeySecret(ConstantPropertiesUtils.SECRECT)
                //.securityToken("<your-token>") // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region(ConstantPropertiesUtils.REGION_Id) // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("阿里云短信测试")
                .templateCode("SMS_154950909")
                .phoneNumbers("19921535102")
                .templateParam("{\"code\":\""+code+"\"}")
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));
        // Asynchronous processing of return values
        /*response.thenAccept(resp -> {
            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> { // Handling exceptions
            System.out.println(throwable.getMessage());
            return null;
        });*/

        // Finally, close the client
        client.close();

        return true;
    }
}
