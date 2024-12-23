package org.example.final_project.service.impl;


import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExportExcelService {
    IOrderDetailRepository orderDetailRepository;
    IOrderRepository orderRepository;
    IUserRepository userRepository;
    OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(Long shopId) {
        List<Long> orderIds = orderDetailRepository.findAllOrderIdsByShopId(shopId);
        List<OrderEntity> listOrderEntity = orderRepository.findByIds(orderIds);
        List<OrderDto> orderDtoList = listOrderEntity.stream().map(orderMapper::toOrderDto).toList();
        return orderDtoList;
    }

    public String sanitizeFileName(String name) {
        return name.replace(":", "_").replace("\\", "_").replace("/", "_");
    }

    public void exportOrderToExcel(long shopId, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();


        Row headerRow = sheet.createRow(0);
        String[] header = {"STT", "Tên Người Mua", "Phương Thức Thanh Toán", "Code", "Số điện thoại", "Địa chỉ", "Trạng Thái Thanh Toán", "Tổng số tiền"};
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }
        List<OrderDto> list = getAllOrders(shopId);
        for (int i = 0; i < list.size(); i++) {
            StringBuilder statusCheckout = new StringBuilder();
            if (list.get(i).getStatusCheckout() == 1) {
                statusCheckout.append("Chờ Thanh Toán");
            } else if (list.get(i).getStatusCheckout() == 2) {
                statusCheckout.append("Thanh Toán Thành Công");
            } else if (list.get(i).getStatusCheckout() == 3) {
                statusCheckout.append("Thanh Toán Thất Bại");
            }
            Optional<UserEntity> userEntity = userRepository.findById(list.get(i).getId());
            UserEntity user = new UserEntity();
            if (userEntity.isPresent()) {
                user = userEntity.get();
            }

            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue(user.getName());
            row.createCell(3).setCellValue(list.get(i).getOrderCode());
            row.createCell(2).setCellValue(list.get(i).getMethodCheckout());
            row.createCell(4).setCellValue(list.get(i).getPhoneReception());
            row.createCell(5).setCellValue(list.get(i).getShippingAddress());
            row.createCell(6).setCellValue(statusCheckout.toString());
            row.createCell(7).setCellValue(list.get(i).getTotalPrice());
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=product.xlsx");
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
}
