package com.xhzb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;

/**
 * RedisTemplate单元测试类
 *
 * @Author mr.wu
 * @Date 2026-8-9 14:42
 */
@SpringBootTest
public class RedisTest {


    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 操作String类型
     */
    @Test
    public void operateString() {
        redisTemplate.opsForValue().set("welcome", "Hello SpringBoot 若依"); //设置值
        System.out.println(redisTemplate.opsForValue().get("welcome"));//获取值

        Long counter = redisTemplate.opsForValue().increment("counter");//按1的步长自增
        System.out.println(counter);
        counter = redisTemplate.opsForValue().increment("counter", 3);//按3的步长自增
        System.out.println(counter);

        counter = redisTemplate.opsForValue().decrement("counter");//按1的步长自减
        System.out.println(counter);
        counter = redisTemplate.opsForValue().decrement("counter", 2);//按2的步长自增减
        System.out.println(counter);
    }

    /**
     * 操作Hash类型
     */
    @Test
    public void operateHash() {
        redisTemplate.opsForHash().put("car:info", "name", "问界"); //添加Hash元素
        redisTemplate.opsForHash().put("car:info", "price", "300000");//添加Hash元素
        redisTemplate.opsForHash().put("car:info", "sales", "100");//添加Hash元素

        Set<Object> keys = redisTemplate.opsForHash().keys("car:info");//获取全部的hash key
        keys.forEach(System.out::println);

        List<Object> values = redisTemplate.opsForHash().values("car:info");//获取全部的hash value
        values.forEach(System.out::println);

        Object price = redisTemplate.opsForHash().get("car:info", "price"); //获取指定hash key的值
        System.out.println(price);

        Long result = redisTemplate.opsForHash().delete("car:info", "price");//根据指定hash key删除元素
        System.out.println(result);
    }

    /**
     * 操作List类型
     * 特点：有序可重复
     */
    @Test
    public void operateList() {
        redisTemplate.opsForList().leftPushAll("queue:list", "a", "b", "c", "d", "c", "d"); //向队列中一次性添加多个元素

        List<String> range = redisTemplate.opsForList().range("queue:list", 0, -1); //获取队列中的全部元素
        range.forEach(System.out::println);

        String result = redisTemplate.opsForList().rightPop("queue:list"); //从队列中获取元素
        System.out.println(result);

        result = redisTemplate.opsForList().rightPop("queue:list");//从队列中获取元素
        System.out.println(result);
    }

    /**
     * 操作Set类型
     * 特点：无序不重复
     */
    @Test
    public void operateSet() {
        redisTemplate.opsForSet().add("brand:set", "vivo", "iphone", "huawei", "小米", "vivo", "huawei"); //一次性向集合中添加多个元素

        Long size = redisTemplate.opsForSet().size("brand:set");//获取元素数量
        System.out.println(size);

        Set<String> members = redisTemplate.opsForSet().members("brand:set");//获取全部元素
        members.forEach(System.out::println);

        Long result = redisTemplate.opsForSet().remove("brand:set", "iphone", "小米");//删除元素
        System.out.println(result);
    }

    /**
     * 操作ZSet/Sorted Set类型
     * 特点：有序不重复
     */
    @Test
    public void operateZSet() {
        //添加元素
        redisTemplate.opsForZSet().add("app:downloads:rank", "qq", 100);
        redisTemplate.opsForZSet().add("app:downloads:rank", "wechat", 200);
        redisTemplate.opsForZSet().add("app:downloads:rank", "tiktok", 300);
        redisTemplate.opsForZSet().add("app:downloads:rank", "weibo", 150);
        redisTemplate.opsForZSet().add("app:downloads:rank", "taobao", 250);
        redisTemplate.opsForZSet().add("app:downloads:rank", "weibo", 20);

        //倒序遍历获取排行榜数据
        Set<String> set = redisTemplate.opsForZSet().reverseRange("app:downloads:rank", 0, -1);
        set.forEach(System.out::println);

        Double score = redisTemplate.opsForZSet().incrementScore("app:downloads:rank", "weibo", 200);//给指定元素自增分值
        System.out.println(score);

        Long size = redisTemplate.opsForZSet().size("app:downloads:rank");//获取元素数量
        System.out.println(size);

        Long result = redisTemplate.opsForZSet().remove("app:downloads:rank", "taobao", "tiktok");//删除元素
        System.out.println(result);
    }
}
