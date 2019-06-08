package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.EvaluationInfoEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;

import java.util.List;

public interface EvaluationInfoService {

    EvaluationInfoEntity queryLastEvaluationByPartitionId(String partitionId);

    String redo(String evaluationId);

    TaskEntity redoLastEvaluationForPartitionId(String partitionId);
}
