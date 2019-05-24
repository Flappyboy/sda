package cn.edu.nju.software.sda.core.service;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;

public interface FunctionService {

    FunctionType getType();

    /**
     * 功能名，应全局唯一
     * @return
     */
    String getName();

    /**
     * 该功能的描述
     * @return
     */
    String getDesc();

    /**
     * 描述该功能所需的输入
     * @return
     */
    MetaData getMetaData();

    /**
     * 判断输入是否符合要求
     * @param inputData
     * @return
     */
    ResultDto check(InputData inputData);

    InfoSet work(InputData inputData, Work work) throws WorkFailedException;
}
