package com.jifenke.lepluslive.topic.service;

import com.jifenke.lepluslive.topic.domain.entities.Topic;
import com.jifenke.lepluslive.topic.repository.ContentRespository;
import com.jifenke.lepluslive.topic.repository.TopicRespository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/12.
 */
@Service
@Transactional(readOnly = true)
public class TopicService {

  @Inject
  private TopicRespository topicRespository;

  @Inject
  private ContentRespository contentRespository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void editTopic(Topic topic) {
    topicRespository.save(topic);

  }

  public List<Topic> findAllTopic() {
    return topicRespository.findAll();
  }

  public Topic findTopicById(Long id) {
    return topicRespository.findOne(id);
  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void deleteTopic(Long id) {
    topicRespository.delete(id);
  }

  public List<Topic> findTopicByPage(Integer offset) {
    if (offset == null) {
      offset = 1;
    }

    return topicRespository.findAll(new PageRequest(offset-1,10)).getContent();
  }

  public Topic findOneTopic(Long id) {
    Topic topic= topicRespository.findOne(id);
    topic.setContent(contentRespository.findByTopic(topic));
    return topic;
  }
}
