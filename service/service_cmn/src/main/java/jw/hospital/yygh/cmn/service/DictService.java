package jw.hospital.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jw.hospital.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response);

    void importDictData(MultipartFile file);

    String getDictName(String s, String value);

    List<Dict> findByDictCode(String dictCode);
}
