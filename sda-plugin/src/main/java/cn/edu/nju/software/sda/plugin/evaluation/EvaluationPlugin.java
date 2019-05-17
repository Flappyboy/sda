package cn.edu.nju.software.sda.plugin.evaluation;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.evaluation.Evaluation;
import cn.edu.nju.software.sda.plugin.base.SdaPlugin;

import java.io.File;
import java.io.IOException;

public interface EvaluationPlugin extends SdaPlugin {

    /**
     * 返回该评估算法是否适合当前划分
     * @param effectiveInfo
     * @return
     */
    boolean match(EffectiveInfo effectiveInfo);

    /**
     * 评估算法
     * @param app 划分的结果和划分时使用的相关信息
     * @param workspace 划分时可能需要用到的临时工作空间
     * @return
     * @throws IOException
     */
    Evaluation evaluate(App app, File workspace) throws IOException;
}
