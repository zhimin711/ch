package com.ch.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
            Address address = new InternetAddress(email);
            return new Address[]{address};
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return new Address[]{};
    }

    public static Address[] convertAddress(String[] addresses) {
        Address[] addressesss = Stream.of(addresses).filter(r -> r != null).toArray(InternetAddress[]::new);
        return new Address[]{};
    }
}
