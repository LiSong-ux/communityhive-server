package net.industryhive.bean.wrap;

import net.industryhive.bean.User;

/**
 * @author 未央
 * @create 2019-11-11 10:27
 */
public class WrapUser extends User {

    //邮箱验证码
    private String emailCode;

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }
}
