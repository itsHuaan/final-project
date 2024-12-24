package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.entity.*;
import org.example.final_project.model.enum_status.CheckoutStatus;
import org.example.final_project.model.enum_status.StatusShipping;
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


    @Override
    public int updateStatusShipping(StatusMessageDto messageDto) {
        Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(messageDto.getOrderId(), messageDto.getShopId());
//        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.shopOrder(messageDto.getShopId(), messageDto.getOrderId());
        notificatioForUser(messageDto);
        if (orderTrackingEntity.isPresent()) {
            OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
            if (messageDto.getStatus() == StatusShipping.Completed.getStatus()) {
                orderTrackingEntity1.setPaidDate(LocalDateTime.now());
                Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(messageDto.getOrderId());
                if (optionalOrderEntity.isPresent()) {
                    OrderEntity orderEntity = optionalOrderEntity.get();
                    orderEntity.setStatusCheckout(CheckoutStatus.Completed.getStatus());
                    orderRepository.save(orderEntity);
                }
            }
            orderTrackingEntity1.setStatus(messageDto.getStatus());
            orderTrackingEntity1.setNote(messageDto.getNote());
            orderTrackingRepository.save(orderTrackingEntity1);
            return 1;
        }
        return 0;
    }

    public void notificatioForUser(StatusMessageDto statusMessageDto) {
        NotificationEntity notificationEntity = new NotificationEntity();
        OrderEntity orderEntity = orderRepository.findById(statusMessageDto.getOrderId()).orElse(null);
        List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findByOrderId(statusMessageDto.getOrderId());

        String OrderCode = orderEntity.getOrderCode();

        UserEntity user = userRepository.findById(statusMessageDto.getShopId()).orElse(null);
        String shopName = user.getShop_name();
        String title = "";
        String content = "";
        String shipping = "SPX Express";
        String image = orderDetailEntity.get(0).getSkuEntity().getImage();
        if (statusMessageDto.getStatus() == StatusShipping.Confirmed.getStatus()) {
            title = "Xác nhận đơn hàng ";
            content = "Đơn hàng " + OrderCode + "đã được Người bán " + shopName + " xác nhận ";
        }
        if (statusMessageDto.getStatus() == StatusShipping.Shipping_confirmed.getStatus()) {
            title = "Đang vận chuyển";
            content = "Đơn hàng " + OrderCode + " đã được Người bán " + shopName + " giao cho đợn vị vận chuyển qua phương thức vận chuyển " + shipping;

        }
        if (statusMessageDto.getStatus() == StatusShipping.Delivering.getStatus()) {
            title = "Bạn có đơn hàng đang trên đường giao ";
            content = "Shipper báo rằng : đơn hàng " + OrderCode + "của bạn đang trong quá trình vận chuyển và dữ kiến giao trong 1-2 ngày tới . Vui lòng bỏ qua thông báo này nếu bạn đang nhận được hàng nhé";
        }
        if (statusMessageDto.getStatus() == StatusShipping.Completed.getStatus()) {
            title = "Xác nhận đã nhận hàng";
            content = "Vui lòng chỉ ấn 'Đã nhận được hàng' khi đơn hàng" + OrderCode + "đã được giao đến bạn và sản phẩm không có vấn đề nào";
        }

        notificationEntity.setUserId(statusMessageDto.getUserId());
        notificationEntity.setTitle(title);
        notificationEntity.setContent(content);
        notificationEntity.setIsRead(0);
        notificationEntity.setCreatedAt(LocalDateTime.now());
        notificationEntity.setImage(image);
        notificationRepository.save(notificationEntity);

    }


}
