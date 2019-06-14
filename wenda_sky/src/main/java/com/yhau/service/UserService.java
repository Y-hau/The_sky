package com.yhau.service;

import com.yhau.core.util.Md5Util;
import com.yhau.dao.LoginTicketDao;
import com.yhau.dao.UserDao;
import com.yhau.model.LoginTicket;
import com.yhau.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserDao userDao;
    @Resource
    private LoginTicketDao loginTicketDao;

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (userDao.selectByName(username) != null) {
            map.put("msg", "用户已存在");
            return map;
        }
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(Md5Util.MD5(password + user.getSalt()));
        userDao.addUser(user);
        String ticket = addLoginTicket(userDao.selectByName(user.getName()).getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        User user = userDao.selectByName(username);
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!Md5Util.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = loginTicketDao.selectbyUserId(userId);
        if (loginTicket != null || loginTicket.getExpired().after(new Date()) || loginTicket.getStatus() == 0) {
            Date now = new Date();
            now.setTime(3600 * 24 * 100 + now.getTime());
            loginTicket.setExpired(now);
            loginTicket.setStatus(0);
            loginTicketDao.updateLoginTicket(loginTicket);
        } else {

            loginTicket = new LoginTicket();
            loginTicket.setUserId(userId);
            loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
            Date now = new Date();
            now.setTime(3600 * 24 * 100 + now.getTime());
            loginTicket.setExpired(now);
            loginTicketDao.addTicket(loginTicket);
        }
        return loginTicket.getTicket();
    }

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    public List<User> getUserNameList() {
        return userDao.selectUserNameList();
    }

}
