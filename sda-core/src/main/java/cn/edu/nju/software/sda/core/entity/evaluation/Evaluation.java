package cn.edu.nju.software.sda.core.entity.evaluation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Evaluation {
    String id;

    Set<Indicator> indicators = new HashSet<>();

    /**
     * 添加评估指标和其值
     * @param indicator
     * @return
     */
    public Evaluation addIndicator(Indicator indicator){
        indicators.add(indicator);
        return this;
    }
}
