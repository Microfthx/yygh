package jw.hospital.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jw.hospital.yygh.model.order.OrderInfo;
import jw.hospital.yygh.order.mapper.OrderMapper;
import jw.hospital.yygh.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        return null;
    }
}
