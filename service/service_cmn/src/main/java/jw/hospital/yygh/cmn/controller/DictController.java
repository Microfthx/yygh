package jw.hospital.yygh.cmn.controller;


import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hospital.yygh.cmn.listener.DictListener;
import jw.hospital.yygh.cmn.service.DictService;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.model.cmn.Dict;
import jw.hospital.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    //导入数据字典
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }
    //导出数据字典接口
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> dictList= dictService.findByDictCode(dictCode);
        return Result.ok(dictList);
    }

    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> dictList= dictService.findChildData(id);
        return Result.ok(dictList);
    }

    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value){
        String dictName = dictService.getDictName("",value);
        return dictName;
    }
}
