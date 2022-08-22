package jw.hospital.yygh.easyexcel;

import com.alibaba.excel.EasyExcel;
import jw.hospital.yygh.cmn.service.DictService;
import jw.hospital.yygh.model.acl.User;
import jw.hospital.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HXLY
 * @PackageName: TestWrite
 * @Description:
 * @date 16:52
 */
public class TestWrite {
    public static void main(String[] args) {
        List<UserData> list = new ArrayList<>();
        for(int i = 0;i<10;i++){
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("testName"+i);
            list.add(userData);
        }

        String fileName = "E:\\excel\\01.xlsx";

        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(list);
    }
}
