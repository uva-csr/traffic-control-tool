package system.monitors;

import controllers.ResourceManager;
import resource.rules.Rule;
import resources.Resource;
import utils.MetricArray;
import utils.RejectException;

public class Monitor {
    /*
    use like below
    try{
        Monitor.entry("resourceName");
        //resources begin
        func(1,2);
        //resources end
        Monitor.exit("resourceName");
    }catch(RejectException e){
        //your method was rejected
    }
    */

    public static void entry(Resource resource) throws RejectException {
        Rule rule = ResourceManager.resourceRuleHashMap.get(resource);
        if(rule == null){
            return;
        }
        boolean ifPass = rule.getMethod().control();
        MetricArray<SystemMetric> metricArray = MetricMonitor.metricMap.get(resource);
        metricArray.currentWindow(System.currentTimeMillis());
        SystemMetric systemMetric = metricArray.getWindowValue(System.currentTimeMillis());
        if(systemMetric != null){
            double limitedCpu = rule.getSystemRule() == null ? 0 : rule.getSystemRule().getLimitedCpu();
            double limitedMemory = rule.getSystemRule() == null ? 0 : rule.getSystemRule().getLimitedMemory();
            double limitedThread = rule.getSystemRule() == null ? 0 : rule.getSystemRule().getLimitedThreadNumber();
            if(systemMetric.CPU_LOAD <= limitedCpu && systemMetric.THREADERS <= limitedThread && systemMetric.MEMORY_USAGE <= limitedMemory){
                ifPass &= true;
            }
        }
        if(ifPass) return;
        throw new RejectException();
    }

    public static void exit(Resource resource){

    }

}
