package org.example.final_project.util;

public class EmailTemplate {

    public static String otpEmailContent(String otp) {
        return "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "    <div style=\"width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #435ebe; padding: 20px; color: #ffffff; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px;\">\n" +
                "            <h2>OTP Verification</h2>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px; color: #333333;\">\n" +
                "            <p>Dear User,</p>\n" +
                "            <p>Thank you for using our service. To complete your verification, please use the OTP code below:</p>\n" +
                "            <p style=\"font-size: 24px; font-weight: bold; color: #435ebe; margin: 20px 0;\">" + otp + "</p>\n" +
                "            <p>This code will expire in 3 minutes. If you did not request this, please ignore this email.</p>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 10px; background-color: #f4f7fa; text-align: center; font-size: 12px; color: #999999; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">\n" +
                "            <p>If you have any questions, feel free to contact our support team.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>";
    }

    public static String forgotPasswordEmailContent(String token) {
        return "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 20px auto; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); background-color: #fff;\">\n" +
                "        <div style=\"background-color: #435ebe; color: white; padding: 10px 20px; border-radius: 5px; font-size: 20px;\">\n" +
                "            Forgot Password Confirmation\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px; font-size: 16px; color: #333;\">\n" +
                "            <p>Hello,</p>\n" +
                "            <p>We received a request to reset your password. To proceed with resetting your password, please click the button below:</p>\n" +
                "            <p style=\"text-align: center;\">\n" +
                "                <a href=\"http://localhost:8080/reset-password?token=" + token + "\"\n" +
                "                   style=\"background-color: #435ebe; color: white; padding: 10px 20px; text-align: center; text-decoration: none; font-size: 16px; border-radius: 5px; display: inline-block;\">\n" +
                "                    Reset Password\n" +
                "                </a>\n" +
                "            </p>\n" +
                "            <p>This link will expire after 24 hours. If you did not request a password reset, please ignore this email.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>";
    }

    public static String shopLockedEmailContent(String reason) {
        return "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "    <div style=\"width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #435ebe; padding: 20px; color: #ffffff; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px;\">\n" +
                "            <h2 style=\"margin: 0;\">Shop Locked Notification</h2>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px; color: #333333;\">\n" +
                "            <p>Dear User,</p>\n" +
                "            <p>We regret to inform you that your shop has been locked for the following reason:</p>\n" +
                "            <p style=\"font-size: 18px; font-weight: bold; color: #d9534f; margin: 20px 0;\">" + reason + "</p>\n" +
                "            <p>If you believe this is a mistake or need further assistance, please contact our support team.</p>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 10px; background-color: #f4f7fa; text-align: center; font-size: 12px; color: #999999; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">\n" +
                "            <p style=\"margin: 0;\">If you have any questions, feel free to contact our support team.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>";
    }
}
