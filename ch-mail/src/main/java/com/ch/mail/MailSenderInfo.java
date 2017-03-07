package com.ch.mail;

/**
 * 描述：com.ch.mail
 *
 * @author 80002023
 *         2017/3/3.
 * @version 1.0
 * @since 1.8
 */
public class MailSenderInfo {


    // 邮件发送者的地址
    private String[] fromAddresses;
    // 邮件接收者的地址
    private String[] toAddresses;
    private String[] ccAddresses;
    // 邮件主题
    private String subject;
    // 邮件的文本内容
    private String content;

    // 邮件附件的文件名
    private String[] attachFileNames;

    public String[] getFromAddresses() {
        return fromAddresses;
    }

    public void setFromAddresses(String[] fromAddresses) {
        this.fromAddresses = fromAddresses;
    }

    public String[] getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(String[] toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String[] getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(String[] ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] attachFileNames) {
        this.attachFileNames = attachFileNames;
    }

}
