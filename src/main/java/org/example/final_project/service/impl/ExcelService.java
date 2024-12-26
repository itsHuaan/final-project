package org.example.final_project.service.impl;


import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.final_project.dto.OrderDetailDto;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.OrderDetailMapper;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelService {
    IOrderDetailRepository orderDetailRepository;
    IOrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    IUserRepository userRepository;

    public List<OrderDto> getAllOrders(Long shopId) {
        List<Long> orderIds = orderDetailRepository.findAllOrderIdsByShopId(shopId);
        List<OrderEntity> listOrderEntity = orderRepository.findByIds(orderIds);
        return listOrderEntity.stream().map(orderMapper::toOrderDto).toList();
    }

    public Double totalPrice(long orderId) {
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailDto> orderDtoList = orderDetailEntities.stream().map(orderDetailMapper::toOrderDto).toList();
        return orderDtoList.stream().mapToDouble(e -> e.getPrice() * e.getQuantity()).sum();
    }
    

    public void exportOrderToExcel(long shopId, HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row headerRow = sheet.createRow(0);
        String[] header = {"STT", "Tên Người Mua", "Email", "Code", "Phương Thức Thanh Toán", "Số điện thoại", "Địa chỉ", "Trạng Thái Thanh Toán", "Tổng số tiền"};
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

        List<OrderDto> list1 = getAllOrders(shopId);

        List<OrderDto> list = list1.stream()
                .filter(t -> t.getCreatedAt().toLocalDate().isAfter(startDate) &&
                        t.getCreatedAt().toLocalDate().isBefore(endDate))
                .toList();
        for (int i = 0; i < list.size(); i++) {
            StringBuilder statusCheckout = new StringBuilder();
            if (list.get(i).getStatusCheckout() == 1) {
                statusCheckout.append("Chờ Thanh Toán");
            } else if (list.get(i).getStatusCheckout() == 2) {
                statusCheckout.append("Thanh Toán Thành Công");
            } else if (list.get(i).getStatusCheckout() == 3) {
                statusCheckout.append("Thanh Toán Thất Bại");
            }

            double totalAmount = totalPrice(list.get(i).getId());
            UserDto user = list.get(i).getUser();
            String customerName = list.get(i).getCustomerName();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(customerName);
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(list.get(i).getOrderCode());
            row.createCell(4).setCellValue(list.get(i).getMethodCheckout());
            row.createCell(5).setCellValue(list.get(i).getPhoneReception());
            row.createCell(6).setCellValue(list.get(i).getShippingAddress());
            row.createCell(7).setCellValue(statusCheckout.toString());
            row.createCell(8).setCellValue(totalAmount);
        }
        Optional<UserEntity> optionalShop = userRepository.findById(shopId);
        String shopName = optionalShop
                .map(UserEntity::getShop_name)
                .orElse("Unknown Shop");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=order" + shopName + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    public void importExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            String code = row.getCell(3).getStringCellValue();
            if (!orderRepository.existsByOrderCode(code)) {
                Map<String, Integer> statusMapping = Map.of(
                        "chothanhtoan", 1,
                        "thanhtoanthanhcong", 2,
                        "thanhtoanthatbai", 3
                );
                String cellValue = row.getCell(7).getStringCellValue();
                String normalizedValue = normalizeString(cellValue);
                Integer status = statusMapping.getOrDefault(normalizedValue, null);

                UserEntity user = userRepository.findByEmail(row.getCell(2).getStringCellValue()).orElse(null);
                OrderEntity orderEntity = OrderEntity.builder()
                        .createdAt(LocalDateTime.now())
                        .methodCheckout(row.getCell(4).getStringCellValue())
                        .orderCode(row.getCell(3).getStringCellValue())
                        .shippingAddress(row.getCell(6).getStringCellValue())
                        .phoneReception(row.getCell(5).getStringCellValue())
                        .statusCheckout(status)
                        .totalPrice(row.getCell(8).getNumericCellValue())
                        .user(user)
                        .build();
                orderRepository.save(orderEntity);
            }
        }
    }

    private static String normalizeString(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase().replaceAll("\\s+", "");
    }
}
