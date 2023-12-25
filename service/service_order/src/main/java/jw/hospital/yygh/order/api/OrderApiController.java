package jw.hospital.yygh.order.api;

import jw.hospital.yygh.common.result.Result;
import jw.hospital.yygh.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    //生成挂号订单
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrders(@PathVariable String scheduleId,
                             @PathVariable Long patientId){
        //获取上传文件

        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }
}
