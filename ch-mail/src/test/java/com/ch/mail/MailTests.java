package com.ch.mail;

import com.ch.utils.CommonUtils;
import com.ch.utils.DateUtils;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 描述：com.ch.mail
 *
 * @author 80002023
 *         2017/3/6.
 * @version 1.0
 * @since 1.8
 */
public class MailTests {

    Object o;

    @Test
    public void loadProp() {
//        o = MailUtils.loadConfig(MailUtils.loadProp());
        o = CommonUtils.isEmail("80002023@sf-express.com");
        System.out.println(o);
    }

    @Test
    public void testSend() {
        SimpleMailSender sender = new SimpleMailSender();
        sender.setConfig(MailUtils.loadConfig(MailUtils.loadProp()));
        MailSenderInfo info = new MailSenderInfo();
        info.setSubject("Test");
        info.setContent("Test Content");
        info.setFromAddresses(new String[]{"80002023@sf-express.com"});
        info.setToAddresses(new String[]{"80002023@sf-express.com"});

        sender.sendTextMail(info);
    }

    public void sendMessage(String host, String from,
                            String fromUserPassword, String to, String subject,
                            String messageText, String messageType) throws MessagingException {
        // 第一步：配置javax.mail.Session对象
        System.out.println("为" + host + "配置mail session对象");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
        props.put("mail.smtp.port", "25"); //google使用465或587端口
        props.put("mail.smtp.auth", "true"); // 使用验证
//        props.put("mail.debug", "true");
        Session mailSession = Session.getInstance(props, new MailAuthenticator(from, fromUserPassword));

        // 第二步：编写消息
        System.out.println("编写消息from——to:" + from + "——" + to);

        InternetAddress fromAddress = new InternetAddress(from);
        InternetAddress toAddress = new InternetAddress(to);

        MimeMessage message = new MimeMessage(mailSession);

        message.setFrom(fromAddress);
        message.addRecipient(MimeMessage.RecipientType.TO, toAddress);

        message.setSentDate(DateUtils.currentTime());
        message.setSubject(subject);
        message.setContent(messageText, messageType);

        // 第三步：发送消息
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, "80002023", fromUserPassword);
        transport.send(message, message.getRecipients(MimeMessage.RecipientType.TO));
        System.out.println("message yes");
    }

    @Test
    public void testSendEmail() {
        try {
            sendMessage("hqmail.sf.com", "80002023@sf-express.com",
                    "Zhimin12344", "80002023@sf-express.com", "Test",
                    "Test Content",
                    "text/html;charset=gb2312");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
