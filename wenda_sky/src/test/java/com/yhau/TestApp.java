package com.yhau;

import com.yhau.core.util.MailUtil;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestApp {
    @Resource
    StringEncryptor encryptor;
    @Resource
    MailUtil mailUtil;

    @Test
    public void getPass() {
        String url = encryptor.encrypt("P@ssw0rd");
        String name = encryptor.encrypt("528089923@qq.com");
        String password = encryptor.encrypt("Yh0815..");

        System.out.println(url + "----------------");
        System.out.println(name + "----------------");
        System.out.println(password + "----------------");
        Assert.assertTrue(name.length() > 0);
        Assert.assertTrue(password.length() > 0);
    }

    @Test
    public void SendMail() {
        mailUtil.sendMail("476665931@qq.com", "nihao", "zhucema");
    }
}