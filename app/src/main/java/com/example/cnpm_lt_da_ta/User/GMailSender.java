package com.example.cnpm_lt_da_ta.User;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender {
    private final String senderEmail = "hoangquocthinh2018@gmail.com"; // Thay bằng email của bạn
    private final String senderPassword = "qbpb kmzc npge ndsd"; // Thay bằng App Password của bạn

    private final Properties props;

    public GMailSender() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    // Phương thức gửi mã xác nhận và trả về mã xác nhận
    public String sendVerificationCode(String recipientEmail) {
        String verificationCode = generateVerificationCode(); // Tạo mã xác nhận

        // Thiết lập kết nối và gửi email
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo nội dung email
            String subject = "Mã xác nhận phục hồi mật khẩu";
            String body = "Mã xác nhận của bạn là: " + verificationCode;

            // Tạo thông tin email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Gửi email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return verificationCode; // Trả về mã xác nhận
    }

    // Hàm sinh mã xác nhận (có thể thay đổi cách thức sinh mã theo yêu cầu)
    private String generateVerificationCode() {
        // Ví dụ mã xác nhận là số ngẫu nhiên 6 chữ số
        int verificationCode = (int) (Math.random() * 1000000);
        return String.format("%06d", verificationCode); // Đảm bảo mã luôn có 6 chữ số
    }
}
