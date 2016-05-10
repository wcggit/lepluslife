package com.jifenke.lepluslive.product.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ScrollPictureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/11.
 */
@RestController
public class ScrollPictureController {

  private Logger LOG = LoggerFactory.getLogger(ScrollPictureController.class);
  @Inject
  private ScrollPictureService scrollPictureService;

  @RequestMapping(value = "/scrollPicture", method = RequestMethod.GET)
  public ModelAndView goScrollPicture(Model model) {
    model.addAttribute("scrollPictures", scrollPictureService.findAllScrollPcitures());
    return MvUtil.go("/product/scrollPictureList");
  }

  @RequestMapping(value = "/scrollPicture", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public LejiaResult editScrollPicture(@RequestBody ScrollPicture scrollPicture) {
    scrollPictureService.editScrollPciture(scrollPicture);
    return LejiaResult.ok("修改成功");
  }


  @RequestMapping(value = "/scrollPicture", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public LejiaResult addScrollPicture(@RequestBody ScrollPicture scrollPicture) {
    if (scrollPicture.getId() != null) {
      LOG.debug("新建轮播图ID为空");
      throw new RuntimeException("新建轮播图ID为空");
    }
    scrollPictureService.createScrollPciture(scrollPicture);
    return LejiaResult.ok("保存成功");
  }

  @RequestMapping(value = "/scrollPicture/{id}", method = RequestMethod.DELETE)
  public LejiaResult deleteScrollPicture(@PathVariable Integer id) {
    scrollPictureService.deleteScrollPciture(id);
    return LejiaResult.ok("删除成功");
  }

  @RequestMapping(value = "/scrollPicture/{id}", method = RequestMethod.GET)
  public ModelAndView goEditScrollPicturePage(@PathVariable Integer id,Model model) {
    model.addAttribute("scrollPicture", scrollPictureService.findOneScrollPicture(id));
    return MvUtil.go("/product/scrollPicture/scrollPictureEdit");
  }

}
