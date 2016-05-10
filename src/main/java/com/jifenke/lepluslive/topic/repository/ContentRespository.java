package com.jifenke.lepluslive.topic.repository;


import com.jifenke.lepluslive.topic.domain.entities.Content;
import com.jifenke.lepluslive.topic.domain.entities.Topic;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/4/12.
 */
public interface ContentRespository extends JpaRepository<Content,Long> {

  Content findByTopic(Topic topic);
}
