package jw.hospital.yygh.oss.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.oss.service
 * @Description:
 * @date 2022/12/01 16:58
 */
@Service
public interface FileService{
    String upload(MultipartFile file);
}
