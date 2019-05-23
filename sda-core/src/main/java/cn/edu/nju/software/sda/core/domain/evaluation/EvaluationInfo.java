package cn.edu.nju.software.sda.core.domain.evaluation;

import cn.edu.nju.software.sda.core.domain.info.Info;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EvaluationInfo extends Info {
    public static final String INFO_NAME_EVALUATION = "INFO_NAME_EVALUATION";

    private Evaluation evaluation;

    public EvaluationInfo(Evaluation evaluation) {
        super(INFO_NAME_EVALUATION);
        this.evaluation = evaluation;
    }
}