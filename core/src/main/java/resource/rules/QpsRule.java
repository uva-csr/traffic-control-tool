package resource.rules;

import lombok.Data;

@Data
public class QpsRule {
    Long limitedQps;

    QpsRule(Long limitedQps){
        this.limitedQps = limitedQps;
    }

    QpsRule(){}
}
