package com.minilink.util;

import com.minilink.config.MyThreadPoolExecutor;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author 徐志斌
 * @description: 发送邮件工具类
 * @date 2023/12/26 16:52
 * @Version: 1.0
 */

@Component
public class EmailUtil {
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 文本邮件
     *
     * @param toEmail 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Async(MyThreadPoolExecutor.THREAD_POOL_NAME)
    public void sendTextMail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * HTML邮件
     *
     * @param toEmail 收件人，多个时参数形式 ："xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param subject 主题
     * @param content 内容
     */
    @Async(MyThreadPoolExecutor.THREAD_POOL_NAME)
    public void sendHtmlMail(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(fromEmail);
        InternetAddress[] internetAddressTo = InternetAddress.parse(toEmail);
        messageHelper.setTo(internetAddressTo);
        message.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }

    /**
     * 带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    @Async(MyThreadPoolExecutor.THREAD_POOL_NAME)
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);
        mailSender.send(message);
    }
}
