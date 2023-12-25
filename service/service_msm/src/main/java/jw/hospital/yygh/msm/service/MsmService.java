package jw.hospital.yygh.msm.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public interface MsmService {
    boolean send(String phone, String code) throws Exception;
}
