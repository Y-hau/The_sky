package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventProducer;
import com.yhau.core.async.EventType;
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

    @Resource
    EventProducer eventProducer;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public Question selectById(int qid) {
        return questionDao.selectById(qid);
    }

    public String addQuestion(String title, String content) {

        Question question = new Question();
        question.setTitle(sensitiveService.filter(HtmlUtils.htmlEscape(title)));
        question.setCommentCount(0);
        question.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(content)));
        question.setCreatedDate(new Date());
        question.setUserId(hostHandler.getUser().getId());
        int res = questionDao.addQuestion(question);
        if (res > 0) {
            eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION).setActorId(question.getUserId())
                    .setEntityId(question.getId()).setExt("title", question.getTitle()).setExt("content", question.getContent()));
            return ResponseUtil.getJSONString(0);
        }
        return ResponseUtil.getJSONString(1, "失败");
    }
}
