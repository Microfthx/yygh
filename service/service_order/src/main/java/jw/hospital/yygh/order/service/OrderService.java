package jw.hospital.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jw.hospital.yygh.model.order.OrderInfo;

public interface OrderService extends IService<OrderInfo> {


    Long saveOrder(String scheduleId, Long patientId);
}
