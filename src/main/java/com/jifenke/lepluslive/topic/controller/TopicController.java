package com.jifenke.lepluslive.topic.controller;

import com.jifenke.lepluslive.topic.domain.entities.Topic;
import com.jifenke.lepluslive.topic.service.TopicService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/15.
 */
@Controller
@RequestMapping("/topic")
public class TopicController {

    @Inject
    private TopicService topicService;

    @ApiOperation(value = "首页左滑加载专题列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Topic> findTopicByPage(@ApiParam(value = "第几页") @RequestParam(value = "page", required = false) Integer offset) {
        return topicService
                .findTopicByPage(offset);
    }

}
