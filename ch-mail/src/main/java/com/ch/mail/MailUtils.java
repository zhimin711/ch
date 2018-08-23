package com.ch.mail;

import com.ch.err.ConfigException;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 描述：com.ch.mail
 *
 * @author 80002023
 *         2017/3/3.
 * @version 1.0
 * @since 1.8
 */
public class MailUtils {

    private static final Logger logger = LoggerFactory.getLogger(MailUtils.class);

    private MailUtils() {
    }

    public static Address[] convertAddress(String email) {
        try {
            if (CommonUtils.isEmail(email)) {
                Address address = new InternetAddress(email);
                return new Address[]{address};
            }
        } catch (AddressException e) {
            logger.error("convertAddress", e);
        }
        return new Address[]{};
    }

    public static Address[] convertAddress(String[] addresses) {
        if (addresses == null) return new Address[]{};
//        Stream.of(addresses).filter(CommonUtils::isEmail).forEach(System.out::println);
        Address[] addressesArr = Stream.of(addresses).filter(CommonUtils::isEmail).map(r -> {
            try {
                return new InternetAddress(r);
            } catch (AddressException e) {
                logger.error("convertAddress", e);
            }
            return null;
        }).filter(Objects::nonNull).toArray(Address[]::new);
        return addressesArr;
    }

    /**
     * 获得邮件SMTP会话属性
     */
    public static Properties newSenderProp(MailSenderConfig cfg) {
        Properties p = new Properties();
        p.put("mail.smtp.host", cfg.getMailServerHost());
        p.put("mail.smtp.port", cfg.getMailServerPort());
        p.put("mail.smtp.auth", cfg.isValidate() ? "true" : "false");
        p.put("mail.debug", cfg.isDebug() ? "true" : "false");
        return p;
    }

    /**
     * 获得邮件SMTP会话属性
     */
    public static Properties loadProp() {
        Properties p = new Properties();
        try {
            InputStream in = MailUtils.class.getResourceAsStream("/mail.properties");
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            logger.error("loadProp Error! please check config properties!", e);
        }
        return p;
    }

    public static MailSenderConfig loadConfig(Properties properties) {
        MailSenderConfig config = new MailSenderConfig();
        String host = (String) properties.get("mail.smtp.host");
        String port = (String) properties.get("mail.smtp.port");
        String auth = (String) properties.get("mail.smtp.auth");
        String debug = (String) properties.get("mail.debug");
        String username = (String) properties.get("mail.auth.username");
        String password = (String) properties.get("mail.auth.password");
        if (StringUtils.isBlank(host)) {
            throw new ConfigException("Mail host must require!");
        }
        config.setMailServerHost(host.trim());
        if (StringUtils.isBlank(port)) {
            throw new ConfigException("Mail port must require!");
        }
        config.setMailServerHost(port.trim());
        if (CommonUtils.isEquals(auth, "true")) {
            config.setValidate(true);
            config.setUsername(username);
            config.setUsername(password);
        }
        if (CommonUtils.isEquals(debug, "true")) {
            config.setDebug(true);
        }
        return config;
    }
}
