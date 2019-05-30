package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.info.Info;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "evaluation_indicator")
public class EvaluationIndicatorEntity implements Serializable {

    @Id
    private String id;

    private String infoId;

    private String name;

    private String value;
}