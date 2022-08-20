package jw.hopistal.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jw.hospital.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);
}
