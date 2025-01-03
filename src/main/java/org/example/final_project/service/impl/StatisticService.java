package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.enumeration.CheckoutStatus;
import org.example.final_project.enumeration.ProductStatus;
import org.example.final_project.enumeration.ShippingStatus;
import org.example.final_project.enumeration.ShopStatus;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.mapper.VariantMapper;
import org.example.final_project.repository.*;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.specification.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.final_project.specification.OrderDetailSpecification.hasShop;
import static org.example.final_project.specification.ProductSpecification.*;
import static org.example.final_project.util.Const.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService implements IStatisticService {
    IProductRepository productRepository;
    ISKURepository skuRepository;
    IOrderDetailRepository orderDetailRepository;
    IUserRepository userRepository;
    UserMapper userMapper;
    ProductMapper productMapper;
    VariantMapper variantMapper;
    IOrderTrackingRepository orderTrackingRepository;


    @Override
    public AdminStatisticDto getAdminStatisticDto() {

        return AdminStatisticDto.builder()
                .totalUsers(getTotalUsers())
                .totalNewUsers(getTotalNewUsers())
                .totalLockedUsers(getTotalLockedUsers())
                .totalShops(getTotalShops())
                .totalLockedShops(getTotalLockedShops())
                .totalPendingShop(getTotalPendingShop())
                .totalRejectedShops(getTotalRejectedProducts())
                .totalLockedProducts(getTotalLockedProducts())
                .totalRejectedProducts(getTotalRejectedProducts())
                .topSellerShops(getTopSellerShops())
                .build();
    }

    @Override
    public List<RevenueStatistic> getRevenueStatistics(long shopId, int year) {
        List<RevenueStatistic> statistics = new ArrayList<>();
        double previousRevenue = 0;
        for (Month month : Month.values()) {
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
            LocalDateTime endTime = startTime.withDayOfMonth(startTime.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            double currentRevenue = getRevenue(shopId, startTime, endTime);
            double growthRate = 0;
            if (previousRevenue != 0) {
                growthRate = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
            }
            statistics.add(new RevenueStatistic(month.getValue(), currentRevenue, (Math.round(growthRate * 100.0) / 100.0)));
            previousRevenue = currentRevenue;
        }
        return statistics;
    }

    @Override
    public List<RevenueStatistic> getRevenueStatistics(int year) {
        List<RevenueStatistic> statistics = new ArrayList<>();
        double previousRevenue = 0;
        for (Month month : Month.values()) {
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
            LocalDateTime endTime = startTime.withDayOfMonth(startTime.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            double currentRevenue = getRevenue(startTime, endTime) * 0.1;
            double growthRate = 0;
            if (previousRevenue != 0) {
                growthRate = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
            }
            statistics.add(new RevenueStatistic(month.getValue(), currentRevenue, (Math.round(growthRate * 100.0) / 100.0)));
            previousRevenue = currentRevenue;
        }
        return statistics;
    }

    @Override
    public RevenueAndGrowthRateStatisticDto getRevenueStatisticsTest(int year) {
        double previousRevenue = 0;
        List<Integer> months = new ArrayList<>();
        List<Double> revenueData = new ArrayList<>();
        List<Double> growthRateData = new ArrayList<>();
        for (Month month : Month.values()) {
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
            LocalDateTime endTime = startTime.withDayOfMonth(startTime.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            double currentRevenue = getRevenue(startTime, endTime) * 0.1;
            double growthRate = 0;
            if (previousRevenue != 0) {
                growthRate = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
            }
            months.add(month.getValue());
            revenueData.add(currentRevenue);
            growthRateData.add(growthRate);
            previousRevenue = currentRevenue;
        }
        return RevenueAndGrowthRateStatisticDto.builder()
                .month(months)
                .dataSet(RevenueAndGrowthRateDto.builder()
                        .revenue("revenue")
                        .revenueData(revenueData)
                        .growthRate("growth-rate")
                        .growthRateData(growthRateData)
                        .build())
                .build();
    }

    @Override
    public RevenueAndGrowthRateStatisticDto getRevenueStatisticsTest(long shopId, int year) {
        double previousRevenue = 0;
        List<Integer> months = new ArrayList<>();
        List<Double> revenueData = new ArrayList<>();
        List<Double> growthRateData = new ArrayList<>();
        for (Month month : Month.values()) {
            LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
            LocalDateTime endTime = startTime.withDayOfMonth(startTime.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            double currentRevenue = getRevenue(shopId, startTime, endTime) * 0.1;
            double growthRate = 0;
            if (previousRevenue != 0) {
                growthRate = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
            }
            months.add(month.getValue());
            revenueData.add(currentRevenue);
            growthRateData.add(growthRate);
            previousRevenue = currentRevenue;
        }
        return RevenueAndGrowthRateStatisticDto.builder()
                .month(months)
                .dataSet(RevenueAndGrowthRateDto.builder()
                        .revenue("revenue")
                        .revenueData(revenueData)
                        .growthRate("growth-rate")
                        .growthRateData(growthRateData)
                        .build())
                .build();
    }

    @Override
    public ShopRatioDto getShopRatioDto() {
        return ShopRatioDto.builder()
                .totalShops(getTotalShops())
                .totalLockedShops((double) (getTotalLockedShops() / getTotalShops()) * 100)
                .totalPendingShop((double) (getTotalPendingShop() / getTotalShops()) * 100)
                .totalRejectedShops((double) (getTotalRejectedShops() / getTotalShops()) * 100)
                .build();
    }

    private StatisticDto buildStatistic(long shopId, LocalDateTime startTime) {
        return StatisticDto.builder()
                .averageRating(getAverageOfRating(shopId, startTime, END_OF_DAY))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, startTime, END_OF_DAY))
                .totalOfProducts(getTotalProducts(shopId, startTime, END_OF_DAY))
                .totalOfOrders(getTotalOfOrders(shopId, startTime, END_OF_DAY))
                .revenue(getRevenue(shopId, startTime, END_OF_DAY))
                .lockedProducts(getLockedProducts(shopId, startTime, END_OF_DAY))
                .totalOfCustomers(getTotalCustomers(shopId, startTime, END_OF_DAY))
                .soldProducts(getSoldProducts(shopId, startTime, END_OF_DAY))
                .build();
    }

    @Override
    public List<PeriodicStatisticDto> getPeriodicStatistics(long shopId) {
        return List.of(
                new PeriodicStatisticDto("Today", buildStatistic(shopId, START_OF_DAY)),
                new PeriodicStatisticDto("This week", buildStatistic(shopId, START_OF_WEEK)),
                new PeriodicStatisticDto("This month", buildStatistic(shopId, START_OF_MONTH)),
                new PeriodicStatisticDto("This year", buildStatistic(shopId, START_OF_YEAR))
        );
    }

    @Override
    public ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return ShopStatisticDto.builder()
                .averageRating(getAverageOfRating(shopId, startTime, endTime))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, startTime, endTime))
                .totalOfProducts(getTotalProducts(shopId, startTime, endTime))
                .orderStatistic(getOrderStatisticDto(shopId, startTime, endTime))
                .revenue(getRevenue(shopId, startTime, endTime))
                .lockedProducts(getLockedProducts(shopId, startTime, endTime))
                .totalOfCustomers(getTotalCustomers(shopId, startTime, endTime))
                .soldProducts(getSoldProducts(shopId, startTime, endTime))
                .topPurchasedUsers(getTopPurchasedUsers(shopId, startTime, endTime))
                .topPurchasedProducts(getTopPurchasedProducts(shopId, startTime, endTime))
                .build();
    }

    private double getAverageOfRating(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isStatus(ProductStatus.ACTIVE.getValue()))
                .and(isValid())
                .and(isBetween(startTime, endTime)));
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll(Specification.where(
                OrderDetailSpecification.hasShop(shopId)));
        Map<ProductEntity, Long> productQuantities = orderDetails.stream()
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getSkuEntity().getProduct(),
                        Collectors.summingLong(OrderDetailEntity::getQuantity)
                ));
        double totalWeightedRating = 0.0;
        long totalSoldQuantity = 0;
        for (ProductEntity product : products) {
            long productSoldQuantity = productQuantities.getOrDefault(product, 0L);
            if (productSoldQuantity > 0) {
                double productWeightedRating = product.getFeedbacks().stream()
                        .mapToDouble(feedback -> feedback.getRate() * productSoldQuantity)
                        .sum();
                totalWeightedRating += productWeightedRating;
                totalSoldQuantity += productSoldQuantity;
            }
        }
        double averageRating = totalSoldQuantity > 0
                ? totalWeightedRating / totalSoldQuantity
                : 0.0;
        return Math.round(averageRating * 100.0) / 100.0;
    }

    private int getTotalOfFeedbacks(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isStatus(ProductStatus.ACTIVE.getValue()))
                .and(isValid())
                .and(isBetween(startTime, endTime))).stream().toList();
        return products.stream()
                .mapToInt(product -> product.getFeedbacks().stream().toList().size())
                .sum();
    }

    private int getTotalProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isNotDeleted())
                .and(isBetween(startTime, endTime))).size();
    }

    private int getTotalOfOrders(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderTrackingRepository.findAll(Specification.where(
                OrderTrackingSpecification.hasShop(shopId)
                        .and(OrderTrackingSpecification.isBetween(startTime, endTime))
        )).size();
    }

    private double getRevenue(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                        hasShop(shopId)
                                .and(OrderDetailSpecification.isBetween(startTime, endTime))
                                .and(OrderDetailSpecification.hasStatus(CheckoutStatus.COMPLETED.getValue()))
                )).stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getPrice())
                .sum();
    }

    private double getRevenue(LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                        OrderDetailSpecification.isBetween(startTime, endTime)
                                .and(OrderDetailSpecification.hasStatus(CheckoutStatus.COMPLETED.getValue()))
                )).stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getPrice())
                .sum();
    }

    @Override
    public Page<CartSkuDto> getLowStockProducts(long shopId, int quantity, Pageable pageable) {
        return skuRepository.findAll(Specification.where(
                        SKUSpecification.hasShop(shopId)
                                .and(SKUSpecification.isValid())
                                .and(SKUSpecification.isLowStock(quantity))
                ), pageable)
                .map(variantMapper::toDto);
    }

    private int getLockedProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(
                hasUserId(shopId)
                        .and(isNotDeleted())
                        .and(isNotStatus(1))
                        .and(isBetween(startTime, endTime))).size();
    }

    private long getTotalCustomers(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(OrderDetailSpecification.distinctUsersByShopIdAndDateRange(shopId, startTime, endTime)).stream()
                .map(orderDetail -> orderDetail.getOrderEntity().getUser().getUserId())
                .distinct()
                .count();
    }

    private long getSoldProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                        OrderDetailSpecification.hasShop(shopId)
                                .and(OrderDetailSpecification.isBetween(startTime, endTime))
                                .and(OrderDetailSpecification.hasStatus(2))
                )).stream()
                .mapToLong(OrderDetailEntity::getQuantity)
                .sum();
    }


    private List<UserFeedBackDto> getTopPurchasedUsers(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findDistinctUsersByShopAndDateRange(shopId, startTime, endTime, PageRequest.of(0, 10)).getContent().stream()
                .map(userMapper::toUserFeedBackDto)
                .toList();
    }

    private List<ProductStatisticDto> getTopPurchasedProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findTopPurchasedProductsByShop(shopId, startTime, endTime,
                        PageRequest.of(0, 10)).getContent().stream()
                .map(productMapper::toProductStatisticDto)
                .toList();
    }

    private OrderStatisticDto getOrderStatisticDto(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return OrderStatisticDto.builder()
                .totalOfOrders(getTotalOfOrders(shopId, startTime, endTime))
                .orderByStatus(buildOrderStatisticByStatusDto(shopId, startTime, endTime))
                .build();
    }

    private List<OrderStatisticByStatusDto> buildOrderStatisticByStatusDto(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return List.of(
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.CREATED),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.PENDING),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.CONFIRMED),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.PENDING_SHIPPING),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.CONFIRMED_SHIPPING),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.DELIVERING),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.DELIVERED),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.PAID),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.COMPLETED),
                getOrderStatisticByStatusDto(shopId, startTime, endTime, ShippingStatus.CANCELLED)
        );
    }

    private OrderStatisticByStatusDto getOrderStatisticByStatusDto(long shopId, LocalDateTime startTime, LocalDateTime endTime, ShippingStatus status) {
        long totalOfOrders = getTotalOfOrders(shopId, startTime, endTime);
        long orderByStatus = orderTrackingRepository.findAll(Specification.where(
                OrderTrackingSpecification.hasShop(shopId)
                        .and(OrderTrackingSpecification.isBetween(startTime, endTime))
                        .and(OrderTrackingSpecification.hasStatus(status.getValue()))
        )).size();
        return OrderStatisticByStatusDto.builder()
                .status(status.toString())
                .order(orderByStatus)
                .ratio(((double) orderByStatus / totalOfOrders) * 100)
                .build();
    }


    //*************************************************************************************

    private long getTotalUsers() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.isNotSuperAdmin())
        )).size();
    }

    private long getTotalNewUsers() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.isNotSuperAdmin())
                        .and(UserSpecification.hasNewlyJoined())
        )).size();
    }

    private long getTotalLockedUsers() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.hasStatus(2))
                        .and(UserSpecification.isNotSuperAdmin())
        )).size();
    }

    private long getTotalShops() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.hasShopStatus(ShopStatus.ACTIVE.getValue()))
                        .and(UserSpecification.isNotSuperAdmin())
        )).size();
    }

    private long getTotalLockedShops() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.isNotSuperAdmin())
                        .and(UserSpecification.hasShopStatus(ShopStatus.LOCKED.getValue()))
        )).size();
    }

    private long getTotalPendingShop() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.isNotSuperAdmin())
                        .and(UserSpecification.hasShopStatus(ShopStatus.PENDING.getValue()))
        )).size();
    }

    private long getTotalRejectedShops() {
        return userRepository.findAll(Specification.where(
                UserSpecification.isNotDeleted()
                        .and(UserSpecification.isNotSuperAdmin())
                        .and(UserSpecification.hasShopStatus(ShopStatus.REJECTED.getValue()))
        )).size();
    }

    private long getTotalLockedProducts() {
        return productRepository.findAll(Specification.where(
                        ProductSpecification.isNotDeleted())
                .and(ProductSpecification.isStatus(ProductStatus.INACTIVE.getValue())
                )).size();
    }

    private long getTotalRejectedProducts() {
        return productRepository.findAll(Specification.where(
                        ProductSpecification.isNotDeleted())
                .and(ProductSpecification.isStatus(ProductStatus.REJECTED.getValue())
                )).size();
    }

    private List<ShopDto> getTopSellerShops() {
        return userRepository.findAll().stream()
                .map(userMapper::toShopDto)
                .filter(shopDto -> shopDto.getSold() > 0
                        && shopDto.getRating() > 0)
                .sorted(Comparator.comparing(ShopDto::getRating).reversed())
                .limit(10)
                .toList();
    }
}
