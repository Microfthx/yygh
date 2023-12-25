package jw.hospital.yygh.oss.controller;

import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.oss.controller
 * @Description:
 * @date 2022/12/01 16:56
 */
@RestController
@RequestMapping("api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;
    //上传
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        //获取上传文件
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
