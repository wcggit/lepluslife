package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.topic.domain.entities.Topic;
import com.jifenke.lepluslive.topic.service.TopicService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/15.
 */
@Controller
@RequestMapping("/weixin")
public class WeiXinTopicController {

  @Inject
  private TopicService topicService;

  @RequestMapping("topic")
  public ModelAndView goTopicPage(){
    return MvUtil.go("/weixin/topic");

  }

  @RequestMapping("topic/{id}")
  public ModelAndView goTopicDetailPage(@PathVariable Long id,Model model){
    model.addAttribute("topic",topicService.findOneTopic(id));

    return MvUtil.go("/weixin/topicDetail");

  }

}
