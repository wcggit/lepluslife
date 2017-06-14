package com.jifenke.lepluslive.statistics.service;

import com.jifenke.lepluslive.global.util.DateUtils;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * redis存储 Created by zhangwen on 2017/3/10.
 */
@Service
public class VisitLogRedisService {

  @Inject
  private RedisTemplate<String, String> redisTemplate;

//  @Resource(name = "stringRedisTemplate")
//  private ZSetOperations<String, String> zSetOperations;
//
//  @Resource(name = "stringRedisTemplate")
//  private SetOperations<String, String> setOperations;

  /**
   * 点击自增  2017/03/10
   *
   * @param user     用户唯一凭证，unionID但不限于unionID
   * @param category 产品详情=product:|公众号按钮=menu:
   * @param target   标明属于哪个具体产品或按钮等唯一识别符
   */
  public void addClickLog(String user, String category, String target) {

//     byte[] rawKey = redisTemplate.getKeySerializer().serialize(category);
//    //pipeline
//    RedisCallback<List<Object>> callback = new RedisCallback<List<Object>>() {
//      @Override
//      public List<Object> doInRedis(RedisConnection redisConnection) throws DataAccessException {
//        redisConnection.zIncrBy(category.getBytes(), 1, target.getBytes());
//        redisConnection.sAdd((category + target).getBytes(), user.getBytes());
//        return null;
//      }
//    };
    //lambda
    RedisCallback<List<Object>> callback = (e) -> {
      e.zIncrBy(category.getBytes(), 1, target.getBytes());//总点击次数
//      e.sAdd((category + "total:" + target).getBytes(), user.getBytes());//总点击人
      e.zIncrBy((category + "day:" + target).getBytes(), 1,
                DateUtils.formatYYYYMMDD(new Date()).getBytes()); //每日点击次数

      return null;
    };
    redisTemplate.executePipelined(callback);

//    //点击次数
//    zSetOperations.incrementScore(category, target, 1);
//    //点击人数
//    setOperations.add(category + target, user);
  }

}
