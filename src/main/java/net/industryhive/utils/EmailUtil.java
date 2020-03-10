package net.industryhive.utils;

import net.industryhive.entity.Email;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author 未央
 * @create 2019-11-09 10:28
 */
public class EmailUtil {

    private static final String HOST = Email.host;
    private static final Integer PORT = Email.port;
    private static final String USERNAME = Email.userName;
    private static final String PASSWORD = Email.passWord;
    private static final String emailForm = Email.emailForm;
    private static final String timeout = Email.timeout;
    private static final String personal = Email.personal;
    private static final String subject = Email.subject;
    private static final String html = Email.html;
    private static JavaMailSenderImpl mailSender = createMailSender();

    /**
     * 邮件发送器
     *
     * @return
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.timeout", timeout);
        //properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", Integer.toString(PORT));//设置端口
        /*
            2020/3/9
            发现亿速云把465端口封了，系统不能自动发送邮件
            从网上找了文章发现SMTP端口除了25和465外，还可以用587端口
            但是如果只是简单地把端口从465换成587，那邮件还是发不出去
            经过尝试，发现把以下三行代码注释掉可以发送成功
         */
        //properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(PORT));//设置ssl端口
        //properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        //properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(properties);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param to 接收人
     * @param html 发送内容
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(String to, String html) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailForm, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
//      messageHelper.addAttachment("", new File(""));//附件
        mailSender.send(mimeMessage);
    }

}
