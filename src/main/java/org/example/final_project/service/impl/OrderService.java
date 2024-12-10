package org.example.final_project.service.impl;

import com.cloudinary.api.exceptions.NotFound;
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
import org.example.final_project.entity.*;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.OrderModel;
import org.example.final_project.model.enum_status.CheckoutStatus;
import org.example.final_project.repository.ICartItemRepository;
import org.example.final_project.repository.ICartRepository;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final PaymentService paymentService;
    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final IOrderDetailRepository orderDetailRepository;

    public String submitCheckout(OrderModel orderModel , HttpServletRequest request) {
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
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setOrderEntity(orderEntity);
                orderDetailEntity.setPrice(cartItemRequest.getPrice());
                orderDetailEntity.setQuantity(cartItemRequest.getQuantity());
                orderDetailEntity.setOption1(cartItemRequest.getOption1());
                orderDetailEntity.setOption2(cartItemRequest.getOption2());
                orderDetailEntity.setShopId(cartItemRequest.getShopId());
                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(cartItemRequest.getProductId());
                orderDetailEntity.setProduct(productEntity);
                orderDetailRepository.save(orderDetailEntity);
            }
        }

        if(method.toLowerCase().equals("vnpay")){
            return paymentService.creatUrlPaymentForVnPay(request);
        }else {
            return "đặt hàng thành công";
        }
    }
    public ApiResponse<?> statusPayment(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        OrderEntity order;
        if(orderEntity.isPresent()) {
            if(status.equals("00")){
                order = orderEntity.get();
                order.setStatusCheckout(CheckoutStatus.Completed.getStatus());
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
}
