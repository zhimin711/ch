package com.ch.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 描述：com.ch.mail
 *
 * @author 80002023
 *         2017/3/3.
 * @version 1.0
 * @since 1.8
 */
public class MailAuthenticator extends Authenticator {

    private String username;
    private String password;

    public MailAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
