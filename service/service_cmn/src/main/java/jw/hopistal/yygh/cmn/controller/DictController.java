package jw.hopistal.yygh.cmn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jw.hopistal.yygh.cmn.service.DictService;
import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData")
    public Result findChildData(@PathVariable Long id){
        List<Dict> dictList= dictService.findChildData(id);
        return Result.ok(dictList);
    }
}
