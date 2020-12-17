package net.industryhive.test;

import net.industryhive.utils.EmailSender;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * @author 未央
 * @create 2019-11-09 11:32
 */
public class EmailTest {

    public void sendEmail() throws UnsupportedEncodingException, MessagingException {
        String to = "546993317@qq.com";
        String html = "您的邮箱验证码为：116303";
        EmailSender.send(to, html);
    }

}
