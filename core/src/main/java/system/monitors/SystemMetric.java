package system.monitors;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import com.sun.management.OperatingSystemMXBean;


public class SystemMetric {

    public ThreadInfo[] threadInfo;
    public MemoryUsage HEAP_MEMORY;
    public MemoryUsage OFF_HEAP_MEMORY;
    public Integer THREADERS;
    public static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    public static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    public double MEMORY_USAGE;


    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    public double CPU_LOAD = osBean.getSystemCpuLoad() * 100;



    public SystemMetric(Long timeMillis){
        HEAP_MEMORY = memoryMXBean.getHeapMemoryUsage();
        OFF_HEAP_MEMORY = memoryMXBean.getNonHeapMemoryUsage();
        threadInfo = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
        THREADERS = threadInfo.length;
        MEMORY_USAGE = (HEAP_MEMORY.getUsed() + OFF_HEAP_MEMORY.getUsed()) * 1.0 / (HEAP_MEMORY.getInit() + OFF_HEAP_MEMORY.getInit());
    }

    public static void main(String[] args){
        SystemMetric s = new SystemMetric(System.currentTimeMillis());
        System.out.println(s.HEAP_MEMORY);
        System.out.println(s.OFF_HEAP_MEMORY);
        System.out.println(s.THREADERS);
        System.out.println(s.CPU_LOAD);
    }


}
