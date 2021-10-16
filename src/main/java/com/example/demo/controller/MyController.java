package com.example.demo.controller;

import com.example.demo.entity.AddReq;
import com.example.demo.entity.DeleteReq;
import com.example.demo.entity.ListResp;
import com.example.demo.utils.RedisKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class MyController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/test")
    public String test(){
        return "test ok!";
    }

    @RequestMapping("/roll")
    public String roll() {
        return redisTemplate.boundSetOps(RedisKey.SHOP_NAME).randomMember();
    }

    @RequestMapping("/list")
    public ListResp list() {
        Set<String> members = redisTemplate.boundSetOps(RedisKey.SHOP_NAME).members();
        ListResp resp = new ListResp();
        resp.setList(members);
        return resp;
    }

    @RequestMapping("/add")
    public String add(@RequestBody AddReq req) {
        List<String> list = req.getList();
        if (list == null || list.size() == 0) {
            return "param error";
        }

        List<String> tempList = new ArrayList<>(list.size());
        for (String s : list) {
            String name = s.trim();
            tempList.add(name);
        }

        String[] array = new String[tempList.size()];
        array = tempList.toArray(array);

        redisTemplate.boundSetOps(RedisKey.SHOP_NAME).add(array);
        return "add" + req + "success!";
    }

    @RequestMapping("/delete")
    public String delete(@RequestBody DeleteReq req) {
        List<String> list = req.getList();
        if (list == null || list.size() == 0) {
            return "param error";
        }

        List<String> tempList = new ArrayList<>(list.size());
        for (String s : list) {
            String name = s.trim();
            tempList.add(name);
        }

        String[] array = new String[tempList.size()];
        array = tempList.toArray(array);

        redisTemplate.boundSetOps(RedisKey.SHOP_NAME).remove(array);
        return "delete" + req + "success";
    }
}
