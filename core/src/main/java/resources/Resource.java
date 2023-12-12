package resources;

import control.methods.Method;
import controllers.ResourceManager;
import resource.rules.Rule;
import system.monitors.MetricMonitor;
import system.monitors.SystemMetric;
import utils.MetricArray;
import utils.Window;

public class Resource {

    String resourceName;
    static int defaultSamples = 10;
    static int defaultIntervalInMs = 1000;



    public Resource(String name){
        resourceName = name;
        MetricArray<SystemMetric> metricArray = new MetricArray<SystemMetric>(defaultSamples,defaultIntervalInMs) {
            @Override
            public SystemMetric newEmptyBucket(long timeMillis) {
                return new SystemMetric(timeMillis);
            }

            @Override
            protected Window<SystemMetric> resetWindowTo(Window<SystemMetric> window, long startTime) {
                return window;
            }
        };
        MetricMonitor.metricMap.put(this,metricArray);
    }

    public boolean combine(Rule resourceRule){
        ResourceManager.resourceRuleHashMap.put(this,resourceRule);
        return true;
    }

}
