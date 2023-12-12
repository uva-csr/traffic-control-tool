package resource.rules;
import lombok.Data;

@Data
public class SystemRule {

    public Double limitedCpu;
    public Double limitedMemory;
    public Integer limitedThreadNumber;

    public SystemRule(double limitedCpu, double limitedMemory, Integer limitedThreadNumber){
        this.limitedCpu = limitedCpu;
        this.limitedMemory = limitedMemory;
        this.limitedThreadNumber = limitedThreadNumber;
    }

    public SystemRule(){}
}
