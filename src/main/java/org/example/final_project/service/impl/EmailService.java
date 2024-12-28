package org.example.final_project.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.EmailModel;
import org.example.final_project.model.OrderModel;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IEmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.final_project.util.FormatVND.formatCurrency;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService implements IEmailService {
    JavaMailSender emailSender;
    IUserRepository userRepository;
    IProductOptionValueRepository productOptionValueRepository;
    ISKURepository iskuRepository;

    @Override
    public boolean sendEmail(EmailModel email) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), true);
            emailSender.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }


    @Async
    public void sendOrderToEmail(OrderModel orderModel, HttpServletRequest request) throws Exception {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        UserEntity user = userRepository.findById(orderModel.getUserId()).get();
        helper.setTo(user.getEmail());
        helper.setSubject("Thông tin đơn hàng ");
        String emailContent = tableProductOfUser(orderModel, request);
        helper.setText(emailContent, true);
        emailSender.send(mimeMessage);
    }

    public String tableProductOfUser(OrderModel orderModel, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        String vnp_TxnRef = (String) request.getAttribute("tex");
        String amount = orderModel.getAmount();

        String userName = orderModel.getCustomerName();

        String formattedAmount = formatCurrency(Double.parseDouble(amount));

        builder.append(String.format("""
                    <html lang="vi">
                      <head>
                        <meta charset="UTF-8" />
                        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                        <title>Shoppee</title>
                        <link href="https://fonts.googleapis.com/css?family=Poppins:ital,wght@0,400;0,600" rel="stylesheet" />
                      </head>
                      <body style="font-family: 'Poppins', Arial, sans-serif; margin: 0; padding: 0;">
                        <table align="center" style="width: 800px; background-color: #ffffff; border-spacing: 0; border-radius: 10px; margin-top: 20px;">
                          <tr>
                            <td style="padding: 20px; text-align: center; background-color: #ecf1fb; border-top-left-radius: 10px; border-top-right-radius: 10px;">
                              <h2 style="font-size: 24px; font-weight: 600; color: #001942;">Cảm ơn %s vì đã sử dụng sản phẩm của chúng tôi</h2>
                              <img src="https://cloudfilesdm.com/postcards/5b305647c0f5e5a664d2cca777f34bf4.png" alt="Confirmed" style="width: 40px; height: 40px; margin-bottom: 8px;">
                              <p style="color: #0067ff; font-size: 14px; font-weight: 500;">Chúng tôi mong sản phẩm sẽ làm hài lòng bạn!</p>
                            </td>
                          </tr>
                          <tr>
                            <td style="background-color: #ecf1fb; padding: 20px;">
                              <p style="font-size: 14px; color: #001942;">Mã đơn hàng: #%s</p>
                            </td>
                          </tr>
                          <tr>
                            <td style="background-color: #ecf1fb; padding: 20px;">
                              <table width="800px" style="background-color: #fff; padding: 10px; border-radius: 10px;">
                              <thead>
                                        <tr>
                                            <th style="padding: 12px; text-align: center; width: 400px;">Tên sản phẩm</th>
                                            <th style="padding: 12px; text-align: center; width: 160px;">Size</th>
                                            <th style="padding: 12px; text-align: center; width: 200px;">Màu</th>
                                            <th style="padding: 12px; text-align: center; width: 120px;">Số lượng</th>
                                            <th style="padding: 12px; text-align: center; width: 300px;">Giá</th>
                                        </tr>
                                    </thead>
                """, userName, vnp_TxnRef));

        if (orderModel.getCartItems() != null && !orderModel.getCartItems().isEmpty()) {
            orderModel.getCartItems().forEach(item -> {
                Optional<SKUEntity> skuEntity = iskuRepository.findById(item.getProductSkuId());
                if (skuEntity.isPresent()) {
                    String productName = item.getNameProduct() != null ? item.getNameProduct() : "Unknown";
                    String option1 = productOptionValueRepository.findById(item.getOption1());
                    String option2 = productOptionValueRepository.findById(item.getOption2());
                    String price = String.valueOf(item.getPrice());
                    String formattedAmount1 = formatCurrency(Double.parseDouble(price));
                    builder.append("<tr style=\"border-bottom: 1px solid #ddd;\">\n")
                            .append("  <td style=\"padding: 10px; font-size: 16px; color: #333;text-align: center; width: 400px;\">").append(productName).append("</td>\n")
                            .append("  <td style=\"padding: 10px; font-size: 16px; color: #333; text-align: center; width: 160px;\">").append(option1).append("</td>\n")
                            .append("  <td style=\"padding: 10px; font-size: 16px; color: #333; text-align: center; width: 200px;\">").append(option2).append("</td>\n")
                            .append("  <td style=\"padding: 10px; font-size: 16px; color: #333; text-align: center;width: 120px;\">").append(item.getQuantity()).append("</td>\n")
                            .append("  <td style=\"padding: 10px; font-size: 16px; color: #333; text-align: center;width: 300px; \">").append(formattedAmount1).append(" VNĐ </td>\n")
                            .append("</tr>");
                }
            });
        }

        builder.append(String.format("""
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding: 20px; background-color: #ffffff; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                              <table width="800px">
                
                                <tr>
                                  <td style="font-size: 16px; font-weight: 600; color: #001942;">Tổng cộng</td>
                                  <td align="right" style="font-size: 16px; font-weight: 600; color: #001942;"> %s VNĐ</td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </body>
                    </html>
                """, formattedAmount));

        return builder.toString();
    }
}
