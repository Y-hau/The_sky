package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.ResponseUtil;
import com.yhau.dao.QuestionDao;
import com.yhau.model.Question;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class QuestionService {
    @Resource
    QuestionDao questionDao;

    @Resource
    HostHandler hostHandler;

    @Resource
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public Question selectById(int qid) {
        return questionDao.selectById(qid);
    }

    public String addQuestion(String title, String content) {

        if (hostHandler.getUser() != null) {
            Question question = new Question();
            question.setTitle(sensitiveService.filter(HtmlUtils.htmlEscape(title)));
            question.setCommentCount(0);
            question.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(content)));
            question.setCreatedDate(new Date());
            question.setUserId(hostHandler.getUser().getId());
            questionDao.addQuestion(question);
            return ResponseUtil.getJSONString(0);
        } else {
            return ResponseUtil.getJSONString(999);
        }
    }
}
