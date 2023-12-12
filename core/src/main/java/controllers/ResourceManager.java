package controllers;

import resource.rules.Rule;
import resources.Resource;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager {
    public static ConcurrentHashMap<Resource, Rule> resourceRuleHashMap = new ConcurrentHashMap<>();

    ResourceManager(){
        resourceRuleHashMap = new ConcurrentHashMap<Resource, Rule>();
    }

    Resource addResource(Resource resource, Rule rule){
        resourceRuleHashMap.put(resource,rule);
        return resource;
    }

    void deleteResource(Resource resource, Rule rule){
        resourceRuleHashMap.remove(resource);
    }
}
