package com.yhau.core.util;

import com.yhau.config.web.BeetlConfig;
import com.yhau.core.beetl.BeetlConfiguration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;


/**
 * https://blog.csdn.net/qiushi_1990/article/details/81477107
 * pringBoot配置文件敏感信息加密，springboot配置文件数据库密码加密jasypt
 * <p>
 * https://www.jianshu.com/p/16cfcfbedecc
 * springboot 邮件发送
 */
@Component
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Resource
    JavaMailSender jms;

    @Resource
    private BeetlConfig beetlConfig;

    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送html邮件
     *
     * @param toUser  收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public void sendMail(String toUser, String subject, String content) {
        try {
            BeetlConfiguration beetlConfiguration = beetlConfig.beetlConfiguration();
            GroupTemplate groupTemplate = beetlConfiguration.getGroupTemplate();
            Template t = groupTemplate.getTemplate("/beetl/VerifyMailTemp.html");
            t.binding("subject", subject);
            t.binding("content", content);
            MimeMessage mimeMessage = jms.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
            // 设置发信人，发信人需要和spring.mail.username配置的一样否则报错
            message.setFrom(from);
            // 设置收信人
            message.setTo(toUser);
            // 设置主题
            message.setSubject(subject);
            // 第二个参数true表示使用HTML语言来编写邮件
            message.setText(t.render(), true);
            //FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/picture.jpg"));
            //helper.addAttachment("图片.jpg", file);//添加带附件的邮件
            //helper.addInline("picture", file);//添加带静态资源的邮件
            jms.send(mimeMessage);

        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());

        }
    }
}
