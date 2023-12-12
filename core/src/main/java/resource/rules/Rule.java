package resource.rules;

import control.methods.Method;
import lombok.Data;

@Data
public class Rule {
    String ruleName;

    QpsRule qpsRule;

    SystemRule systemRule;

    Method method;

    public Rule(String ruleName){
        this.ruleName = ruleName;
        qpsRule = new QpsRule();
        systemRule = new SystemRule();
    }

    public void setQpsRule(QpsRule qpsRule){
        this.qpsRule = qpsRule;
    }

    public void setSystemRule(SystemRule sysRule){
        this.systemRule = sysRule;
    }

    public void setMethod(Method method){
        this.method = method;
    }
}
