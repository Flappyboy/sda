package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.service.FunctionService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    void newTask(String appId, FunctionService function, InputData inputData);

    @Transactional(propagation = Propagation.REQUIRED)
    void doTask(String appId, FunctionService function, InputData inputData);
}
