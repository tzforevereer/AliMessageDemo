package com.demo.aliservice.controller;


import com.demo.aliservice.common.R;
import com.demo.aliservice.service.AliService;
import com.demo.aliservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msm")
public class AliController {
    @Autowired
    private AliService aliService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public R sedMsm(@PathVariable String phone){
        // 1. 从redis中获取验证码，获取不到就发送
        String code1 = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code1)){
            return R.ok();
        }

        //生成随机值，传递阿里云进行发送
        String code = RandomUtil.getFourBitRandom();

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("code",code);

        boolean isSend =  aliService.send(param,phone);
        if(isSend){
            //发送成功，把成功的验证码放到redis中，设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error().message("短信发送失败");
        }

    }
}
