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

    private String[] fromAddresses;
    private String[] toAddresses;
    private String[] ccAddresses;

    private String subject;
    private String content;

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
}
