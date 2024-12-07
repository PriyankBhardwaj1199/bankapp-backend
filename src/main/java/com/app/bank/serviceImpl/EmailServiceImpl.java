package com.app.bank.serviceImpl;

import com.app.bank.dto.EmailDetails;
import com.app.bank.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderMailAddress;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(senderMailAddress);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessage());

            javaMailSender.send(mailMessage);

            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendBankStatementViaEmail(EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(senderMailAddress);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessage());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            if (emailDetails.getAttachment() != null) {
                mimeMessageHelper.addAttachment(emailDetails.getAttachmentName(), new ByteArrayDataSource(emailDetails.getAttachment(), "application/pdf"));
            }

            javaMailSender.send(mimeMessage);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
