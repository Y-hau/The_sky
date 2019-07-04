package com.yhau.service;

import com.yhau.core.util.StaticUtil;
import com.yhau.model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    @Resource
    private SolrClient solrClient;

    public List<Question> searchQuestion(String keyword, int offset, int count,
                                         String hlPre, String hlPos) throws IOException, SolrServerException {
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl", StaticUtil.QUESTION_TITLE_FIELD + "," + StaticUtil.QUESTION_CONTENT_FIELD);
        QueryResponse response = solrClient.query(query);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(StaticUtil.QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(StaticUtil.QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }
            if (entry.getValue().containsKey(StaticUtil.QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(StaticUtil.QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        return questionList;
    }

    public boolean indexQuestion(int qid, String title, String content) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(StaticUtil.QUESTION_TITLE_FIELD, title);
        doc.setField(StaticUtil.QUESTION_CONTENT_FIELD, content);
        UpdateResponse response = solrClient.add(doc);
        return response != null && response.getStatus() == 0;
    }
}
