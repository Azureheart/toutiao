package com.zjm.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.zjm.toutiao.service.QiniuService;
import com.zjm.toutiao.util.JedisAdapter;
import com.zjm.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){//将model添加到队列中去
        try {
            String json= JSONObject.toJSONString(model);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return  false;
        }

    }
}
