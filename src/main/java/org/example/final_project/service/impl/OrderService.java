package org.example.final_project.service.impl;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.final_project.configuration.VnPay.PaymentService;
import org.example.final_project.configuration.VnPay.VnPayUtil;
import org.example.final_project.dto.*;
import org.example.final_project.entity.*;
import org.example.final_project.enumeration.CheckoutStatus;
import org.example.final_project.enumeration.ShippingStatus;
import org.example.final_project.mapper.*;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.OrderModel;
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
import static org.example.final_project.util.FormatVND.formatCurrency;


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
    private final IHistoryStatusShippingRepository historyStatusShippingRepository;
    private final SKUMapper skuMapper;


    @Override
    public String checkOrderCode(String orderCode) {
        if(orderRepository.existsByOrderCode(orderCode)) {
            return VnPayUtil.getRandomNumber(8);
        }
        return orderCode;
    }

    @Override
    public String submitCheckout(OrderModel orderModel, HttpServletRequest request) throws Exception {
//        int result = processCheckout(orderModel);
//        if (result == 1) {
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
        orderEntity.setCustomerName(orderModel.getCustomerName());
        orderEntity.setStatusCheckout(CheckoutStatus.PENDING.getValue());
        orderRepository.save(orderEntity);
        saveDataOrderTrackingAndDetail(orderModel, orderEntity);
        if (method.equalsIgnoreCase("vnpay")) {
            return paymentService.creatUrlPaymentForVnPay(request);
        } else {

            emailService.sendOrderToEmail(orderModel, request);
            long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
            List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findByOrderId(id);
            orderDetailEntity.forEach(orderDetail -> cartItemRepository.deleteByCartId(orderDetail.getCartDetailId()));
            sentNotificationSuccessForShop(orderEntity, orderDetailEntity);
            deleteQuantity(orderDetailEntity);
            return "Order Complete";
        }
    }

    public int processCheckout(OrderModel orderModel) {
        List<CartItemRequest> list = orderModel.getCartItems();
        for (CartItemRequest cartItemRequest : list) {
            SKUEntity skuEntity = skuRepository.findbySKUId(cartItemRequest.getProductSkuId()).orElse(null);
            if (skuEntity == null) {
                throw new IllegalArgumentException("SKU not found");
            }
            SKUDto skuDto = skuMapper.convertToDto(skuEntity);
            double newPrice = cartItemRequest.getPrice();
            double skuNewPrice = skuDto.getNewPrice();
            if (newPrice == skuNewPrice) {
                return 1;
            }

        }
        return 0;
    }

    public int loadDataCod(OrderModel orderModel) {
        if (orderModel.getCartItems() != null) {
            for (CartItemRequest cartItemRequest : orderModel.getCartItems()) {
                int result = checkQuatity(cartItemRequest.getProductSkuId(), cartItemRequest.getQuantity());
                if (result == 0) {
                    Optional<SKUEntity> skuEntity = skuRepository.findById(cartItemRequest.getProductSkuId());
                    if (skuEntity.isPresent()) {
                        SKUEntity skuEntity1 = skuEntity.get();
                        skuEntity1.setQuantity(skuEntity1.getQuantity() - cartItemRequest.getQuantity());
                        cartItemRepository.deleteByCartId(cartItemRequest.getCartDetailId());
                        skuRepository.save(skuEntity1);
                        return 1;
                    }

                }
            }
        }
        return 0;
    }

    @Override
    public int checkQuatity(long skuId, long currentQuatity) {
        Optional<SKUEntity> skuEntity = skuRepository.findById(skuId);
        if (skuEntity.isPresent()) {
            SKUEntity skuEntity1 = skuEntity.get();
            if (skuEntity1.getQuantity() < currentQuatity) {
                return 1;
            } else {
                return 0;
            }
        }
        return 3;
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
                            .status(ShippingStatus.CREATED.getValue())
                            .order(orderEntity)
                            .shopId(cartItemRequest.getShopId())
                            .createdAt(LocalDateTime.now())
                            .build();

                    orderTrackingRepository.save(trackingEntity);
                    HistoryStatusShippingEntity historyStatusShippingEntity = HistoryStatusShippingEntity.builder()
                            .orderTracking(trackingEntity)
                            .status(ShippingStatus.CREATED.getValue())
                            .createdChangeStatus(LocalDateTime.now())
                            .build();
                    historyStatusShippingRepository.save(historyStatusShippingEntity);
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
    public void deleteQuantity(List<OrderDetailEntity> orderDetails){
        orderDetails.forEach(orderDetail -> {
            Optional<SKUEntity> skuEntityOpt = skuRepository.findById(orderDetail.getSkuEntity().getId());
            if (skuEntityOpt.isPresent()) {
                SKUEntity skuEntity = skuEntityOpt.get();
                skuEntity.setQuantity(skuEntity.getQuantity() - orderDetail.getQuantity());
                skuRepository.save(skuEntity);
            }
        });
    }

    @Override
    public ApiResponse<?> getPaymentStatus(HttpServletRequest request) throws Exception {
        String status = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        long id = orderRepository.findIdByOrderCode(vnp_TxnRef);
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);

        if (orderEntity.isPresent()) {
            OrderEntity order = orderEntity.get();
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(id);

            if (status.equals("00")) {
                orderDetails.forEach(orderDetail -> cartItemRepository.deleteByCartId(orderDetail.getCartDetailId()));
                order.setStatusCheckout(CheckoutStatus.COMPLETED.getValue());
                sentNotificationSuccessForShop(order, orderDetails);
                deleteQuantity(orderDetails);
//                orderDetails.forEach(orderDetail -> {
//                    Optional<SKUEntity> skuEntityOpt = skuRepository.findById(orderDetail.getSkuEntity().getId());
//                    if (skuEntityOpt.isPresent()) {
//                        SKUEntity skuEntity = skuEntityOpt.get();
//                        skuEntity.setQuantity(skuEntity.getQuantity() - orderDetail.getQuantity());
//                        skuRepository.save(skuEntity);
//                    }
//                });

                OrderModel orderModel = new OrderModel();
                orderModel.setUserId(order.getUser().getUserId());
                orderModel.setAmount(String.valueOf(order.getTotalPrice()));
                orderModel.setCustomerName(order.getCustomerName());
                orderModel.setCartItems(orderDetails.stream()
                        .map(OrderDetailMapper::toDTO)
                        .collect(Collectors.toList()));
                request.setAttribute("tex", order.getOrderCode());
                emailService.sendOrderToEmail(orderModel, request);

                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Successful Payment ", null);
            } else {
                sentNotificationFailForUser(order, orderDetails);
                order.setStatusCheckout(CheckoutStatus.FAILED.getValue());
                List<OrderTrackingEntity> orderTrackingEntities = orderTrackingRepository.listOrderTracking(order.getId());
                orderTrackingEntities.forEach(entity -> entity.setStatus(ShippingStatus.CANCELLED.getValue()));
                orderTrackingRepository.saveAll(orderTrackingEntities);
                orderRepository.save(order);
                return createResponse(HttpStatus.OK, "Failed Payment ", null);
            }
        }

        return createResponse(HttpStatus.NOT_FOUND, "Not Found User ", null);
    }


    public void sentNotificationSuccessForShop(OrderEntity orderEntity, List<OrderDetailEntity> orderDetailEntity) {
        for (OrderDetailEntity cartItemRequest1 : orderDetailEntity) {
            SKUEntity skuEntity = skuRepository.findById(cartItemRequest1.getSkuEntity().getId()).orElse(null);
            double total = cartItemRequest1.getQuantity() * cartItemRequest1.getPrice();

            if (skuEntity == null) {
                throw new IllegalArgumentException("Not found Sku");
            }
            SKUDto skuDto = skuMapper.convertToDto(skuEntity);
            String vnd = formatCurrency(total);
            NotificationEntity notificationEntity = NotificationEntity.builder()
                    .image(skuDto.getImage())
                    .title("Đơn hàng mới vừa được tạo ")
                    .content("Đơn hàng " + orderEntity.getOrderCode() + " vừa được tạo đặt với số tiền " + vnd + " VNĐ .")
                    .shopId(cartItemRequest1.getShopId())
                    .isRead(0)
                    .userId(orderEntity.getUser().getUserId())
                    .orderCode(orderEntity.getOrderCode())
                    .createdAt(LocalDateTime.now())
                    .shopUserId(cartItemRequest1.getShopId())
                    .orderId(orderEntity.getId())
                    .build();
//            notificationEntity.setAdminId(0L);
            iNotificationRepository.save(notificationEntity);
        }
    }

    public void sentNotificationFailForUser(OrderEntity orderEntity, List<OrderDetailEntity> orderDetailEntity) {
        for (OrderDetailEntity cartItemRequest1 : orderDetailEntity) {
            SKUEntity skuEntity = skuRepository.findById(cartItemRequest1.getSkuEntity().getId()).orElse(null);


            if (skuEntity == null) {
                throw new IllegalArgumentException("Not found Sku");
            }
            SKUDto skuDto = skuMapper.convertToDto(skuEntity);
            double total = cartItemRequest1.getQuantity() * cartItemRequest1.getPrice();


            String vnd = formatCurrency(total);


            NotificationEntity notificationEntity = NotificationEntity.builder()
                    .image(skuDto.getImage())
                    .title("Đơn hàng thanh toán thất bại ")
                    .content("Đơn hàng " + orderEntity.getOrderCode() + " với số tiền " + vnd + " VNĐ " + " thanh toán thất bại .")
                    .isRead(0)
                    .orderCode(orderEntity.getOrderCode())
                    .userId(orderEntity.getUser().getUserId())
                    .createdAt(LocalDateTime.now())
                    .orderId(orderEntity.getId())
                    .shopUserId(cartItemRequest1.getShopId())
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
