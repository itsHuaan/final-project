package org.example.final_project.service.impl;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.final_project.configuration.VnPay.PaymentService;
import org.example.final_project.dto.*;
import org.example.final_project.entity.*;
import org.example.final_project.mapper.OrderDetailMapper;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.mapper.OrderTrackingMapper;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.OrderModel;
import org.example.final_project.model.enum_status.CheckoutStatus;
import org.example.final_project.model.enum_status.StatusShipping;
import org.example.final_project.repository.*;
import org.example.final_project.service.IOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final ISKURepository skuRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final ICartItemRepository cartItemRepository;
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final INotificationRepository iNotificationRepository;


    @Override
    public String submitCheckout(OrderModel orderModel, HttpServletRequest request) throws Exception {
        String vnp_TxnRef = (String) request.getAttribute("tex");
        String method = orderModel.getMethodCheckout();
        double totalPrice = Double.parseDouble(orderModel.getAmount());
        OrderEntity orderEntity = OrderMapper.toOrderEntity(orderModel);
        orderEntity.setUser(UserEntity.builder()
                .userId(orderModel.getUserId())
                .build());
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setOrderCode(vnp_TxnRef);
        orderEntity.setPhoneReception(orderModel.getPhoneReception());
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setStatusCheckout(CheckoutStatus.Pending.getStatus());
        orderRepository.save(orderEntity);
        saveDataOrderTrackingAndDetail(orderModel, orderEntity);
        if (method.equalsIgnoreCase("vnpay")) {
            return paymentService.creatUrlPaymentForVnPay(request);
        } else {
            loadDataCod(orderModel);
            emailService.sendOrderToEmail(orderModel, request);
            long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
            List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findByOrderId(id);
            sentNotificationfoShop(orderEntity, orderDetailEntity);
            return "đặt hàng thành công";
        }
    }

    public void loadDataCod(OrderModel orderModel) {
        if (orderModel.getCartItems() != null) {
            for (CartItemRequest cartItemRequest : orderModel.getCartItems()) {
                Optional<SKUEntity> skuEntity = skuRepository.findById(cartItemRequest.getProductSkuId());
                if (skuEntity.isPresent()) {
                    SKUEntity skuEntity1 = skuEntity.get();
                    skuEntity1.setQuantity(skuEntity1.getQuantity() - cartItemRequest.getQuantity());
                    cartItemRepository.deleteByCartId(cartItemRequest.getCartDetailId());
                    skuRepository.save(skuEntity1);
                }
            }
        }
    }

    public void saveDataOrderTrackingAndDetail(OrderModel orderModel, OrderEntity orderEntity) {
        if (orderModel.getCartItems() != null) {
            for (CartItemRequest cartItemRequest : orderModel.getCartItems()) {
                Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(orderEntity.getId(), cartItemRequest.getShopId());
                if (orderTrackingEntity.isPresent()) {
                    OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
                    orderTrackingRepository.save(orderTrackingEntity1);
                } else {
                    OrderTrackingEntity trackingEntity = OrderTrackingEntity.builder()
                            .status(StatusShipping.Create.getStatus())
                            .order(orderEntity)
                            .shopId(cartItemRequest.getShopId())
                            .createdAt(LocalDateTime.now())
                            .build();

                    orderTrackingRepository.save(trackingEntity);

                }
                OrderDetailEntity orderDetailEntity = OrderDetailMapper.toEntity(cartItemRequest);
                orderDetailEntity.setOrderEntity(orderEntity);
                SKUEntity skuEntity = new SKUEntity();
                skuEntity.setId(cartItemRequest.getProductSkuId());
                orderDetailEntity.setSkuEntity(skuEntity);
                orderDetailRepository.save(orderDetailEntity);
            }
        }
    }

    @Override
    public ApiResponse<?> statusPayment(HttpServletRequest request) throws Exception {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);

        if (orderEntity.isPresent()) {
            OrderEntity order = orderEntity.get();
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(id);
            orderDetails.forEach(orderDetail -> cartItemRepository.deleteByCartId(orderDetail.getCartDetailId()));

            if (status.equals("00")) {
                order.setStatusCheckout(CheckoutStatus.Completed.getStatus());
                sentNotificationfoShop(order, orderDetails);

                orderDetails.forEach(orderDetail -> {
                    Optional<SKUEntity> skuEntityOpt = skuRepository.findById(orderDetail.getSkuEntity().getId());
                    if (skuEntityOpt.isPresent()) {
                        SKUEntity skuEntity = skuEntityOpt.get();
                        skuEntity.setQuantity(skuEntity.getQuantity() - orderDetail.getQuantity());
                        skuRepository.save(skuEntity);
                    }
                });

                OrderModel orderModel = new OrderModel();
                orderModel.setUserId(order.getUser().getUserId());
                orderModel.setAmount(String.valueOf(order.getTotalPrice()));
                orderModel.setCartItems(orderDetails.stream()
                        .map(OrderDetailMapper::toDTO)
                        .collect(Collectors.toList()));
                request.setAttribute("tex", order.getOrderCode());
                emailService.sendOrderToEmail(orderModel, request);

                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Successful Payment ", null);
            } else {
                order.setStatusCheckout(CheckoutStatus.Failed.getStatus());
                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Failed Payment ", null);
            }
        }

        return createResponse(HttpStatus.NOT_FOUND, "Not Found User ", null);
    }


    public void sentNotificationfoShop(OrderEntity orderEntity, List<OrderDetailEntity> orderDetailEntity) {
        for (OrderDetailEntity cartItemRequest1 : orderDetailEntity) {
            SKUEntity skuEntity = skuRepository.findById(cartItemRequest1.getSkuEntity().getId()).orElse(null);
            double total = cartItemRequest1.getQuantity() * cartItemRequest1.getPrice();
            assert skuEntity != null;
            NotificationEntity notificationEntity = NotificationEntity.builder()
                    .image(skuEntity.getImage())
                    .title("Đơn hàng mới vừa được tạo ")
                    .content("Mã đơn : " + orderEntity.getOrderCode() + " vừa được đặt với số tiền là : " + total)
                    .shopId(cartItemRequest1.getShopId())
                    .isRead(0)
                    .userId(orderEntity.getUser().getUserId())
                    .createdAt(LocalDateTime.now())
                    .build();
//            notificationEntity.setAdminId(0L);
            iNotificationRepository.save(notificationEntity);
        }
    }


    @Override
    public OrderModel sentNotify(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        OrderEntity order;
        if (status.equals("00")) {
            order = orderEntity.orElseThrow(() -> new EntityNotFoundException("Order not found "));
            OrderModel orderModel = new OrderModel();
            orderModel.setUserId(order.getUser().getUserId());
            orderModel.setAmount(String.valueOf(order.getTotalPrice()));
            List<CartItemRequest> cartItemRequest = order.getOrderDetailEntities().stream().map(OrderDetailMapper::toDTO).toList();
            orderModel.setCartItems(cartItemRequest);
            return orderModel;
        } else {
            return null;
        }

    }

    @Override
    public ApiResponse<?> getOrdersByShopId(long shopId, Integer pageIndex, Integer pageSize, Integer statusShipping) {
        List<Long> orderIds;

        if (statusShipping != null) {
            orderIds = orderTrackingRepository.findOrderIdsByShopIdAndStatus(shopId, statusShipping);
        } else {
            orderIds = orderDetailRepository.findOrderIdsByShopId(shopId);
        }

        if (orderIds.isEmpty()) {
            return createResponse(HttpStatus.OK, "No orders found", Page.empty());
        }

        if (pageIndex == null || pageSize == null) {
            List<OrderEntity> orderEntities = orderRepository.findAllSortById(orderIds, Sort.by(Sort.Order.desc("createdAt")));
            List<OrderDto> orderDtos = orderEntities.stream().map(orderMapper::toOrderDto).toList();
            return createResponse(HttpStatus.OK, "Successfully Retrieved Orders", orderDtos);
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        Page<OrderEntity> orderEntities = orderRepository.findAllByIds(orderIds, pageable);
        Page<OrderDto> orderDtos = orderEntities.map(orderMapper::toOrderDto);
        return createResponse(HttpStatus.OK, "Successfully Retrieved Orders", orderDtos);
    }

    @Override
    public ApiResponse<?> getOrderTracking(Long orderId, Long shopId) {
        List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.shopOrder(shopId, orderId);
        List<OrderDetailDto> orderDetailDtos = orderDetailEntity.stream().map(orderDetailMapper::toOrderDto).toList();
        Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(orderId, shopId);
        OrderTrackingEntity orderTrackingEntity1;
        if (orderTrackingEntity.isPresent()) {
            orderTrackingEntity1 = orderTrackingEntity.get();
        } else {
            return createResponse(HttpStatus.NOT_FOUND, "Order Tracking not found", null);
        }
        OrderTrackingDto orderTrackingDto = OrderTrackingMapper.toOrderTrackingDto(orderTrackingEntity1);

        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        OrderEntity orderEntity1 = new OrderEntity();
        if (orderEntity.isPresent()) {
            orderEntity1 = orderEntity.get();
        }
        OrderDto orderDto = orderMapper.toOrderDto(orderEntity1);

        OrderTotalDto orderTotalDto = OrderTotalDto.builder()
                .orderTracking(orderTrackingDto)
                .order(orderDto)
                .orderDetails(orderDetailDtos)
                .build();

        return createResponse(HttpStatus.OK, "Successfully Retrieved Order Details", orderTotalDto);
    }


    @Override
    public String getTotalPrice(String tex) {
        Double amount = orderRepository.findAmountByOrderCode(tex);
        return String.valueOf(amount);

    }

    @Override
    public OrderDto findByShopIdAndCodeOrder(long shopId, String orderCode) {
        Optional<OrderEntity> orderEntity = orderRepository.findOrderIdByShopIdAndOrderCode(shopId, orderCode);
        OrderEntity orderEntity1;
        orderEntity1 = orderEntity.orElseGet(OrderEntity::new);
        return orderMapper.toOrderDto(orderEntity1);
    }

    @Override
    public ApiResponse<?> checkQuatityInStock(long skuId, long currentQuatity) {
        Optional<SKUEntity> skuEntity = skuRepository.findById(skuId);
        if (skuEntity.isPresent()) {
            SKUEntity skuEntity1 = skuEntity.get();
            if (skuEntity1.getQuantity() < currentQuatity) {
                return createResponse(HttpStatus.BAD_REQUEST, "The current quantity is greater than the quantity in the stock", null);
            } else {
                return createResponse(HttpStatus.OK, "The current quantity matches the quantity in stock", null);
            }
        }
        return createResponse(HttpStatus.NOT_FOUND, "Not Found Product ", null);
    }

    @Override
    public ApiResponse<?> getAllUserBoughtOfThisShop(long shopId, Integer page, Integer size) {
        List<Long> listUserIds = orderDetailRepository.findAllCustomerBoughtTheMostAtThisShop(shopId);
        if (page == null || size == null) {
            List<UserEntity> userEntityList = userRepository.findByUserId(listUserIds);
            List<UserDto> list1 = userEntityList.stream().map(userMapper::toDto).toList();
            return createResponse(HttpStatus.OK, "Successfully Retrieved Users", list1);
        }
        if (page < 0 || size <= 0) {
            return createResponse(HttpStatus.OK, "Page must be >= 0 and size must be >= 1 ", null);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> userEntities = userRepository.findByUserId(listUserIds, pageable);
        Page<UserDto> pageDtos = userEntities.map(userMapper::toDto);
        return createResponse(HttpStatus.OK, "Successfully Retrieved Users", pageDtos);

    }
}
