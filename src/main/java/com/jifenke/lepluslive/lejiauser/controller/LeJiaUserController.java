package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.controller.dto.LeJiaUserDto;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.lejiauser.service.SmsService;
import com.jifenke.lepluslive.lejiauser.service.ValidateCodeService;
import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;

import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;

import com.jifenke.lepluslive.topic.domain.entities.Topic;
import com.jifenke.lepluslive.topic.service.TopicService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wcg on 16/4/21.
 */
@Controller
@RequestMapping("/user")
public class LeJiaUserController {

    @Inject
    private LeJiaUserService leJiaUserService;

    @Inject
    private ScoreAService scoreAService;

    @Inject
    private ScoreBService scoreBService;

    @Inject
    private SmsService smsService;

    @Inject
    private ValidateCodeService validateCodeService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private TopicService topicService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    LeJiaUserDto getLeJiaUser(@RequestParam(required = false) String token) {
        LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
        ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(leJiaUser);
        ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);

        return new LeJiaUserDto(scoreA.getScore(), scoreB.getScore(), leJiaUser.getOneBarCodeUrl());
    }

    /**
     * @param phoneNumber
     * @param type        1=注册  2=找回
     * @return
     */
    @ApiOperation(value = "注册和找回密码时发送验证码")
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult sendCode(@RequestParam(required = false) String phoneNumber,
                         @ApiParam(value = "1=注册  2=找回") @RequestParam(required = false) Integer type) {
        LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
        if (type == 1) {
            if (leJiaUser != null) {
                return LejiaResult.build(201, "该手机号已注册");
            }
        } else {
            if (leJiaUser == null) {
                return LejiaResult.build(205, "该手机号未注册");
            }
        }

        smsService.saveValidateCode(phoneNumber);
        return LejiaResult.build(200, "发送成功");
    }

    /**
     * @param phoneNumber 手机号
     * @param code        验证码
     * @param token       推送token
     * @throws IOException
     */
    @ApiOperation(value = "点击注册按钮")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult register(@RequestParam(required = false) String phoneNumber,
                         @RequestParam(required = false) String code,
                         @RequestParam(required = false) String token) throws IOException {
        LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
        if (leJiaUser != null) {
            return LejiaResult.build(201, "该手机号已注册");
        }
        Boolean b = validateCodeService.findByPhoneNumberAndCode(phoneNumber, code); //验证码是否正确
        if (!b) {
            return LejiaResult.build(202, "验证码错误");
        }

        LeJiaUserDto leJiaUserDto = leJiaUserService.register(phoneNumber, token);

        return LejiaResult.build(200, "注册成功", leJiaUserDto);
    }

    /**
     * 三种情况设置密码
     * 1.注册 2.发送验证码重置密码时设置密码 3.验证旧密码重置密码
     *
     * @param type 1=注册和发送验证码   2=验证旧密码
     * @return
     */
    @ApiOperation(value = "三种情况设置密码")
    @RequestMapping(value = "/setPwd", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult setPwd(@RequestParam(required = false) String phoneNumber,
                       @ApiParam(value = "旧密码 type=2时必须有") @RequestParam(required = false) String oldPwd,
                       @RequestParam(required = false) String pwd,
                       @ApiParam(value = "1=注册和发送验证码   2=验证旧密码") @RequestParam(required = false) Integer type) {
        LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
        if (type == 1) {
            leJiaUserService.setPwd(leJiaUser, pwd);
        } else {
            if (!leJiaUser.getPwd().equals(MD5Util.MD5Encode(oldPwd, null))) {
                return LejiaResult.build(204, "密码错误");
            }
            leJiaUserService.setPwd(leJiaUser, pwd);
        }

        return LejiaResult.build(200, "设置密码成功");
    }

    /**
     * app登录
     */
    @ApiOperation(value = "点击登录按钮")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult login(@RequestParam(required = false) String phoneNumber,
                      @RequestParam(required = false) String pwd,
                      @ApiParam(value = "推送token") @RequestParam(required = false) String token) {
        LeJiaUser leJiaUser = leJiaUserService.login(phoneNumber, pwd, token);
        if (leJiaUser == null) {
            return LejiaResult.build(204, "密码错误");
        }
        ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(leJiaUser);
        ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
        return LejiaResult.build(200, "登录成功", new LeJiaUserDto(scoreA.getScore(), scoreB.getScore(),
                leJiaUser.getOneBarCodeUrl(), leJiaUser.getUserSid(), leJiaUser.getHeadImageUrl()));
    }

    @ApiOperation(value = "发送验证码重置密码时验证验证码")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult validate(@RequestParam(required = false) String phoneNumber,
                         @RequestParam(required = false) String code) {
        Boolean b = validateCodeService.findByPhoneNumberAndCode(phoneNumber, code); //验证码是否正确
        if (!b) {
            return LejiaResult.build(202, "验证码错误");
        }
        return LejiaResult.build(200, "验证码正确");
    }

    @ApiOperation(value = "打开软件")
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult open(@ApiParam(value = "用户身份标识token") @RequestParam(required = false) String token,
                       @ApiParam(value = "经度(保留六位小数)") @RequestParam(required = false) Double longitude,
                       @ApiParam(value = "纬度(保留六位小数)") @RequestParam(required = false) Double latitude) {

        List<Object> list = new ArrayList<>();
        List<MerchantDto> merchantDtoList = null;
        if (longitude == null || latitude == null) {
            merchantDtoList = merchantService.findMerchantsByPage(1).stream()
                    .map(merchant -> {
                        MerchantDto merchantDto = new MerchantDto();
                        try {
                            BeanUtils.copyProperties(merchantDto, merchant);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return merchantDto;
                    }).collect(Collectors.toList());
        } else {
            merchantDtoList = merchantService.findOrderByDistance(latitude, longitude);
        }
        List<Topic> topicList = topicService.findTopicByPage(1);
        list.add(merchantDtoList);
        list.add(topicList);
        if (token != null) {
            LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
            if (leJiaUser != null) {
                ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(leJiaUser);
                ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
                LeJiaUserDto leJiaUserDto = new LeJiaUserDto(scoreA.getScore(), scoreB.getScore(),
                        leJiaUser.getOneBarCodeUrl(), leJiaUser.getUserSid(), leJiaUser.getHeadImageUrl());
                list.add(leJiaUserDto);
            }
        }
        return LejiaResult.build(200, "ok", list);
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView gotest() {
        return MvUtil.go("/test/test");
    }

}
