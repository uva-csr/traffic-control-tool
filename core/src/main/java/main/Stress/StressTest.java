package main.Stress;

import control.methods.LeakyBucket;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import resource.rules.Rule;
import resource.rules.SystemRule;
import resources.Resource;
import system.monitors.Monitor;
import utils.RejectException;

import java.util.HashMap;

@RestController
class StressTest {

    Resource resource = new Resource("testResource");
    Rule rule = new Rule("testRule");

    @Autowired
    RedisTemplate<Object, Object> jedis;

    @GetMapping("/api/test1")
    public String testEndpoint() {
        rule.setMethod(new LeakyBucket(100,100));
        rule.setSystemRule(new SystemRule(0.3,0.3,50));
        resource.combine(rule);
        String test = "";
        try{
            Monitor.entry(resource);
            jedis.opsForValue().set("key", "value");
            String value = (String) jedis.opsForValue().get("key");
            test += "Redis key value: " + value + "--" + System.currentTimeMillis();
            Monitor.exit(resource);
        }catch (RejectException e){
            test += "rejected at " + System.currentTimeMillis();
        }
        return test;
    }

    @GetMapping("/api/test2")
    public String testEndpoint2() {
        jedis.opsForValue().set("key", "value");
        String value = (String) jedis.opsForValue().get("key");
        return value;
    }
}
