package com.ch.mail;

import com.ch.utils.DateUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 简单邮件（不带附件的邮件）发送器
 *
 * @author 80002023
 *         2017/3/3.
 * @version 1.0
 * @since 1.8
 */
public class SimpleMailSender {

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) {

        Properties properties = new Properties();
        // 判断是否需要身份认证
        MailAuthenticator authenticator = new MailAuthenticator();

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
        // 根据session创建一个邮件消息
        Message message = new MimeMessage(sendMailSession);
        try {
            // 创建邮件发送者地址
            Address[] from = MailUtils.convertAddress(mailInfo.getFromAddresses());
            // 设置邮件消息的发送者
            message.addFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address[] to = MailUtils.convertAddress(mailInfo.getToAddresses());
            message.setRecipients(Message.RecipientType.TO, to);

            Address[] cc = MailUtils.convertAddress(mailInfo.getCcAddresses());
            message.setRecipients(Message.RecipientType.CC, cc);
            // 设置邮件消息的主题
            message.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            message.setSentDate(DateUtils.currentTime());
            // 设置邮件消息的主要内容
            message.setText(mailInfo.getContent());
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }


}
