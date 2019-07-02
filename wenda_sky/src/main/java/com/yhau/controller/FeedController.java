package com.yhau.controller;

import com.yhau.model.Feed;
import com.yhau.service.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    private FeedService feedService;

    @RequestMapping(value = {"/pushFeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String pushFeeds(Model model) {
        try {
            List<Feed> feeds = feedService.getPushFeeds(0, 10);
            model.addAttribute("feeds", feeds);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "/feeds.html";
    }

    @RequestMapping(value = {"/pullFeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String pullFeeds(Model model) {
        try {
            List<Feed> feeds = feedService.getPullFeeds();
            model.addAttribute("feeds", feeds);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "/feeds.html";
    }
}
