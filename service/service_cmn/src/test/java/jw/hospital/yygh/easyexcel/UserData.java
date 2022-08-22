package jw.hospital.yygh.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author HXLY
 * @ClassName: UserData
 * @Description:
 * @date 16:49
 */
@Data
public class UserData {

    @ExcelProperty("用户编号")
    private int uid;

    @ExcelProperty("用户名称")
    private String username;


}
