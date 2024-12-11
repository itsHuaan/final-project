package org.example.final_project.service.impl;

import com.cloudinary.api.exceptions.NotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Order;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.VnPay.PaymentService;
import org.example.final_project.configuration.VnPay.VnPayConfig;
import org.example.final_project.configuration.VnPay.VnPayUtil;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.entity.*;
import org.example.final_project.mapper.OrderDetailMapper;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.OrderModel;
import org.example.final_project.model.enum_status.ActivateStatus;
import org.example.final_project.model.enum_status.CheckoutStatus;
import org.example.final_project.repository.*;
import org.example.final_project.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final PaymentService paymentService;
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderTrackingRepository orderTrackingRepository;
    private final EmailService emailService;
    private final OrderMapper orderMapper;
    private final ISKURepository skuRepository;;

    @Override
    public String submitCheckout(OrderModel orderModel , HttpServletRequest request) throws Exception {
        String vnp_TxnRef = (String) request.getAttribute("tex");
        String method = orderModel.getMethodCheckout();
        double totalPrice = Double.parseDouble(orderModel.getAmount());
        OrderEntity orderEntity = OrderMapper.toOrderEntity(orderModel);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(orderModel.getUserId());
        orderEntity.setUser(userEntity);
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setOrderCode(vnp_TxnRef);
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setStatusCheckout(CheckoutStatus.Pending.getStatus());
        orderRepository.save(orderEntity);




        if(orderModel.getCartItems() != null) {
            for (CartItemRequest cartItemRequest : orderModel.getCartItems()) {
                Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(orderEntity.getId(), cartItemRequest.getShopId());
                if(orderTrackingEntity.isPresent()) {
                    OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
                    orderTrackingRepository.save(orderTrackingEntity1);
                }else {
                    OrderTrackingEntity trackingEntity = new OrderTrackingEntity();
                    trackingEntity.setStatus(ActivateStatus.NotConfirmed.getValue());
                    trackingEntity.setOrder(orderEntity);
                    trackingEntity.setShopId(cartItemRequest.getShopId());
                    trackingEntity.setCreatedAt(LocalDateTime.now());
                    orderTrackingRepository.save(trackingEntity);
                }

                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setOrderEntity(orderEntity);
                orderDetailEntity.setPrice(cartItemRequest.getPrice());
                orderDetailEntity.setQuantity(cartItemRequest.getQuantity());
                orderDetailEntity.setOption1(cartItemRequest.getOption1());
                orderDetailEntity.setOption2(cartItemRequest.getOption2());
                orderDetailEntity.setShopId(cartItemRequest.getShopId());
                SKUEntity skuEntity = new SKUEntity();
                skuEntity.setId(cartItemRequest.getProductSkuId());
                orderDetailEntity.setSkuEntity(skuEntity);
                orderDetailRepository.save(orderDetailEntity);
            }
        }
        if(method.toLowerCase().equals("vnpay")){
            return paymentService.creatUrlPaymentForVnPay(request);
        }else {
            if (orderModel.getCartItems() != null){
                for (CartItemRequest cartItemRequest : orderModel.getCartItems()) {
                    Optional<SKUEntity> skuEntity = skuRepository.findById(cartItemRequest.getProductSkuId());
                    if(skuEntity.isPresent()) {
                        SKUEntity skuEntity1 = skuEntity.get();
                        skuEntity1.setQuantity( skuEntity1.getQuantity() - cartItemRequest.getQuantity());
                        skuRepository.save(skuEntity1);
                    }
                }
            }
            emailService.sendOrderToEmail(orderModel,request);
            return "đặt hàng thành công";
        }
    }


    @Override
    public ApiResponse<?> statusPayment(HttpServletRequest request) throws Exception {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        OrderEntity order;
        if(orderEntity.isPresent()) {
            if(status.equals("00")){
                order = orderEntity.get();
                order.setStatusCheckout(CheckoutStatus.Completed.getStatus());
                OrderModel orderModel = new OrderModel();
                orderModel.setUserId(order.getUser().getUserId());
                orderModel.setAmount(String.valueOf(order.getTotalPrice()));
                List<CartItemRequest> cartItemRequest = order.getOrderDetailEntities().stream().map(e->OrderDetailMapper.toDTO(e)).toList();
                orderModel.setCartItems(cartItemRequest);
                request.setAttribute("tex",order.getOrderCode());
                emailService.sendOrderToEmail(orderModel,request);
                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Successful Payment ", null);
            }else {
                order = orderEntity.get();
                order.setStatusCheckout(CheckoutStatus.Failed.getStatus());
                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Failed Payment ", null);
            }

        }
        return createResponse(HttpStatus.NOT_FOUND, "Not Found User ", null);
    }

    @Override
    public ApiResponse<?> getOrderByShopIdAndOrderId(long shopId) {
        List<Long> orderIds = orderDetailRepository.findOrderIdsByShopId(shopId);
        if (orderIds.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No Orders Found for Shop", Collections.emptyList());
        }
        List<OrderEntity> orderEntities = orderRepository.findAllById(orderIds);
        List<OrderDto> orderDtos = orderEntities.stream().map(e->orderMapper.toOrderDto(e)).collect(Collectors.toList());
        return createResponse(HttpStatus.OK, "Successfully Retrieved Order Details", orderDtos);
    }

    @Override
    public String getTotalPrice(String tex){
        double amount = orderRepository.findAmountByOrderCode(tex);
        return String.valueOf(amount);

    }
}
