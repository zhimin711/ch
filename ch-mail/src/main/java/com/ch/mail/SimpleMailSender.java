package com.ch.mail;

import com.ch.utils.DateUtils;
import com.ch.utils.StringUtils;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 简单邮件（不带附件的邮件）发送器
 *
 * @author 80002023
 *         2017/3/3.
 * @version 1.0
 * @since 1.8
 */
public class SimpleMailSender {

    private MailSenderConfig config;

    private Session session;

    public void setConfig(MailSenderConfig config) {
        this.config = config;
    }

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) {
        if (!openSession()) {
            return false;
        }
        try {
            // 根据session创建一个邮件消息
            Message message = new MimeMessage(session);
            // 创建邮件发送者地址
            Address[] from = MailUtils.convertAddress(mailInfo.getFromAddresses());
            Address[] to = MailUtils.convertAddress(mailInfo.getToAddresses());
            if (from.length <= 0 || to.length <= 0) {
                System.out.println("EMAIL INFO ERROR!");
                return false;
            }
            // 设置邮件消息的发送者
            message.addFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            message.setRecipients(Message.RecipientType.TO, to);

            Address[] cc = MailUtils.convertAddress(mailInfo.getCcAddresses());
            if (cc.length > 0) {
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            // 设置邮件消息的主题
            message.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            message.setSentDate(DateUtils.currentTime());
            // 设置邮件消息的主要内容
            message.setText(mailInfo.getContent());
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public boolean sendHtmlMail(MailSenderInfo mailInfo) {
        if (!openSession()) {
            return false;
        }

        try {
            // 根据session创建一个邮件消息
            Message message = new MimeMessage(session);
            // 创建邮件发送者地址
            Address[] from = MailUtils.convertAddress(mailInfo.getFromAddresses());
            Address[] to = MailUtils.convertAddress(mailInfo.getToAddresses());
            Address[] cc = MailUtils.convertAddress(mailInfo.getCcAddresses());
            message.addFrom(from);
            message.setRecipients(Message.RecipientType.TO, to);
            if (cc.length > 0) {
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            message.setSubject(mailInfo.getSubject());
            message.setSentDate(DateUtils.currentTime());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            message.setContent(mainPart);
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected boolean openSession() {
        if (config == null || StringUtils.isBlank(config.getMailServerHost())) {
            return false;
        }
//        Session sendMailSession;
        // 判断是否需要身份认证
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        if (config.isValidate()) {
            MailAuthenticator authenticator = new MailAuthenticator(config.getUsername(), config.getPassword());
            session = Session.getDefaultInstance(MailUtils.newSenderProp(config), authenticator);
        } else {
            session = Session.getInstance(MailUtils.newSenderProp(config));
        }
        return true;
    }
}
