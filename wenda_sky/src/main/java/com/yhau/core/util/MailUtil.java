package com.yhau.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/*https://www.jianshu.com/p/16cfcfbedecc*/

/**
 * https://blog.csdn.net/qiushi_1990/article/details/81477107
 * pringBoot配置文件敏感信息加密，springboot配置文件数据库密码加密jasypt
 */
@Component
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    @Resource
    private JavaMailSender jms;

    /**
     * 发送邮箱
     *
     * @param toUser  收件人
     * @param subject 设置主题
     * @param text    设置内容
     */
    public void sendMail(String toUser, String subject, String text) {
        subject = "感谢您的注册~";
        text = "感谢你的注册，你的默认密码是：" + "<h3>" + text + "</h3>";

        try {
            MimeMessage mimeMessage = jms.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            // 设置发信人，发信人需要和spring.mail.username配置的一样否则报错
            message.setFrom("1310072293@qq.com");
            // 设置收信人
            message.setTo(toUser);
            // 设置主题
            message.setSubject(subject);
            // 第二个参数true表示使用HTML语言来编写邮件
            message.setText(text, true);
            //FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/picture.jpg"));
            //helper.addAttachment("图片.jpg", file);//添加带附件的邮件
            //helper.addInline("picture", file);//添加带静态资源的邮件
            jms.send(mimeMessage);

        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
    }

}
