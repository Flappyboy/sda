package cn.edu.nju.software.sda.core.domain.evaluation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Evaluation {
    String id;

    List<Indicator> indicators = new ArrayList<>();

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
