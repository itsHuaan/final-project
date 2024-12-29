package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.entity.*;
import org.example.final_project.enumeration.CheckoutStatus;
import org.example.final_project.enumeration.ShippingStatus;
import org.example.final_project.mapper.SKUMapper;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.repository.*;
import org.example.final_project.service.IOrderTrackingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderTrackingService implements IOrderTrackingService {
    IOrderTrackingRepository orderTrackingRepository;
    IOrderDetailRepository orderDetailRepository;
    IOrderRepository orderRepository;
    IUserRepository userRepository;
    INotificationRepository notificationRepository;
    SKUMapper skuMapper;
    private final IHistoryStatusShippingRepository iHistoryStatusShippingRepository;


    @Override
    public int updateStatusShipping(StatusMessageDto messageDto) {
        Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(messageDto.getOrderId(), messageDto.getShopId());
        notificationForUser(messageDto);
        if (orderTrackingEntity.isPresent()) {
            OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
            if(messageDto.getStatus() == ShippingStatus.DELIVERED.getValue()){
                orderTrackingEntity1.setPaidDate(LocalDateTime.now());
            }
            if (messageDto.getStatus() == ShippingStatus.COMPLETED.getValue()) {
                orderTrackingEntity1.setStatus(messageDto.getStatus());
                orderTrackingEntity1.setNote(messageDto.getNote());
                orderTrackingRepository.save(orderTrackingEntity1);
                Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(messageDto.getOrderId());
                if (optionalOrderEntity.isPresent() && checkPaidDate(messageDto.getOrderId()) == 1) {
                    OrderEntity orderEntity = optionalOrderEntity.get();
                    orderEntity.setStatusCheckout(CheckoutStatus.COMPLETED.getValue());
                    orderRepository.save(orderEntity);
                }
            }
            HistoryStatusShippingEntity historyStatusShippingEntity = HistoryStatusShippingEntity.builder()
                    .orderTracking(orderTrackingEntity1)
                    .status(messageDto.getStatus())
                    .createdChangeStatus(LocalDateTime.now())
                    .build();
            iHistoryStatusShippingRepository.save(historyStatusShippingEntity);
            return 1;
        }
        return 0;
    }

    public void timeSet(OrderTrackingEntity orderTrackingEntity){
        if(orderTrackingEntity.getStatus() == 6 && orderTrackingEntity.getPaidDate() != null){
            LocalDateTime paidDatePlusOneDay = orderTrackingEntity.getPaidDate().plusDays(1);
            LocalDateTime now = LocalDateTime.now();

            if(now.isEqual(paidDatePlusOneDay) || now.isAfter(paidDatePlusOneDay)){
                orderTrackingEntity.setStatus(ShippingStatus.COMPLETED.getValue());
                orderTrackingRepository.save(orderTrackingEntity);
                Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderTrackingEntity.getOrder().getId());
                if (optionalOrderEntity.isPresent()) {
                    OrderEntity orderEntity = optionalOrderEntity.get();
                    orderEntity.setStatusCheckout(CheckoutStatus.COMPLETED.getValue());
                    orderRepository.save(orderEntity);
                }
            }
        }
    }


    public int checkPaidDate(Long orderId) {
        boolean isCompleted = orderTrackingRepository.listOrderTracking(orderId)
                .stream()
                .anyMatch(orderTrackingEntity -> orderTrackingEntity.getStatus() == ShippingStatus.COMPLETED.getValue());
        return isCompleted ? 1 : 0;
    };




    public void notificationForUser(StatusMessageDto statusMessageDto) {

        OrderEntity orderEntity = orderRepository.findById(statusMessageDto.getOrderId()).orElse(null);
        List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findByOrderId(statusMessageDto.getOrderId());

        assert orderEntity != null;
        String OrderCode = orderEntity.getOrderCode();

        UserEntity user = userRepository.findById(statusMessageDto.getShopId()).orElse(null);

        SKUEntity skuEntity = orderDetailEntity.get(0).getSkuEntity();
        SKUDto skuDto = skuMapper.convertToDto(skuEntity);


        if (user == null) {
            throw new IllegalStateException("user is null");
        }
        String shopName = user.getShop_name();
        String title;
        String content;
        String shipping = "SPX Express";
        String image = skuDto.getImage();


        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setImage(image);
        notificationModel.setOrderCode(OrderCode);
        if (statusMessageDto.getStatus() == ShippingStatus.CONFIRMED.getValue()) {
            title = "Xác nhận đơn hàng ";
            content = "Đơn hàng " + OrderCode + "đã được Người bán " + shopName + " xác nhận ";
            notificationModel.setTitle(title);
            notificationModel.setContent(content);
            saveNotification(statusMessageDto, notificationModel);
        }
        if (statusMessageDto.getStatus() == ShippingStatus.CONFIRMED_SHIPPING.getValue()) {
            title = "Đang vận chuyển";
            content = "Đơn hàng " + OrderCode + " đã được Người bán " + shopName + " giao cho đợn vị vận chuyển qua phương thức vận chuyển " + shipping;
            notificationModel.setTitle(title);
            notificationModel.setContent(content);
            saveNotification(statusMessageDto, notificationModel);

        }
        if (statusMessageDto.getStatus() == ShippingStatus.DELIVERING.getValue()) {
            title = "Bạn có đơn hàng đang trên đường giao ";
            content = "Shipper báo rằng : đơn hàng " + OrderCode + "của bạn đang trong quá trình vận chuyển và dữ kiến giao trong 1-2 ngày tới . Vui lòng bỏ qua thông báo này nếu bạn đang nhận được hàng nhé";
            notificationModel.setTitle(title);
            notificationModel.setContent(content);
            saveNotification(statusMessageDto, notificationModel);
        }
        if (statusMessageDto.getStatus() == ShippingStatus.DELIVERED.getValue()) {
            title = "Xác nhận đã nhận hàng";
            content = "Vui lòng chỉ ấn 'Đã nhận được hàng' khi đơn hàng" + OrderCode + "đã được giao đến bạn và sản phẩm không có vấn đề nào";
            notificationModel.setTitle(title);
            notificationModel.setContent(content);
            saveNotification(statusMessageDto, notificationModel);
        }
    }





    public void saveNotification(StatusMessageDto statusMessageDto, NotificationModel notificationModel) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .userId(statusMessageDto.getUserId())
                .title(notificationModel.getTitle())
                .content(notificationModel.getContent())
                .isRead(0)
                .createdAt(LocalDateTime.now())
                .image(notificationModel.getImage())
                .orderCode(notificationModel.getOrderCode())
                .build();
        notificationRepository.save(notificationEntity);
    }


}
