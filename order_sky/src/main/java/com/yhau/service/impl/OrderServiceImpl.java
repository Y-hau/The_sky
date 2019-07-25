package com.yhau.service.impl;

import com.yhau.dto.OrderDTO;
import com.yhau.enums.OrderStatusEnum;
import com.yhau.enums.PayStatusEnum;
import com.yhau.model.OrderDetail;
import com.yhau.model.OrderMaster;
import com.yhau.product.client.ProductClient;
import com.yhau.product.common.DecreaseStockInput;
import com.yhau.product.common.ProductInfoOutput;
import com.yhau.repository.OrderDetailRepository;
import com.yhau.repository.OrderMasterRepository;
import com.yhau.service.OrderService;
import com.yhau.utils.KeyUtil;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDetailRepository orderDetailRespository;
    @Resource
    private OrderMasterRepository orderMasterRepository;
    @Resource
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        //查询商品信息(调用商品服务)
        List<String> orderDetails = orderDTO.getOrderDetails().stream().map(
                OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfoOutput> productInfoOutputs = productClient.listForOrder(orderDetails);

        //计算总价
        BigDecimal orderAmout = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetails()) {
            for (ProductInfoOutput productInfo : productInfoOutputs) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    //单价*数量
                    orderAmout = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    //订单详情入库
                    orderDetailRespository.save(orderDetail);
                }
            }
        }

        //扣库存(调用商品服务)
        List<DecreaseStockInput> decreaseStockInputs = orderDTO.getOrderDetails().stream().map(
                e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputs);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }
}
