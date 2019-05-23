package cn.edu.nju.software.sda.plugin.evaluation;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.plugin.base.SdaPluginFunction;

import java.io.File;
import java.io.IOException;

public interface EvaluationAlgorithm extends SdaPluginFunction {

    /**
     * 评估算法
     * @param app 划分的结果和划分时使用的相关信息
     * @param workspace 划分时可能需要用到的临时工作空间
     * @return
     * @throws IOException
     */
    Evaluation evaluate(App app, File workspace) throws IOException;
}
