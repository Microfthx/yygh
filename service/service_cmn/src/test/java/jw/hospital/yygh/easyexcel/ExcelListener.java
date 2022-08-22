package jw.hospital.yygh.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.easyexcel
 * @Description:
 * @date 2022/08/22 17:25
 */
public class ExcelListener extends AnalysisEventListener<UserData> {

    //一行一行
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext){
        System.out.println(userData);
    }

    //一行一行
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext analysisContext){
        System.out.println("表头信息"+headMap);
    }

    //读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext){

    }
}
