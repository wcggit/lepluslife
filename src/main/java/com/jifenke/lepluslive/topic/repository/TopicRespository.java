package com.jifenke.lepluslive.topic.repository;

import com.jifenke.lepluslive.topic.domain.entities.Topic;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/4/12.
 */
public interface TopicRespository extends JpaRepository<Topic,Long> {

}
