package system.monitors;

import resources.Resource;
import utils.MetricArray;

import java.util.concurrent.ConcurrentHashMap;

public class MetricMonitor {
    // TODO: monitor system metrics
    public static ConcurrentHashMap<Resource, MetricArray> metricMap = new ConcurrentHashMap<>();

    boolean put(Resource resource, MetricArray<SystemMetric> metricArray){
        metricMap.put(resource,metricArray);
        return true;
    }
}
