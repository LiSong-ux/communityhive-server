package net.industryhive.test;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author 未央
 * @create 2019-12-02 15:20
 */
public class DateTest {

    public static void main(String args[]) throws InterruptedException {
        Date d1 = new Date();
        Thread.sleep(2600);
        Date d2 = new Date();
        System.out.println(DateUtil.betweenMs(d1, d2));

    }

}
