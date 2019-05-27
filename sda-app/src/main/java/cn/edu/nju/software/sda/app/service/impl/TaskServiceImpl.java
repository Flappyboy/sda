package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.TaskMapper;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void newTask(String appId, FunctionService function, InputData inputData) {
        TaskEntity taskEntity = new TaskEntity();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doTask(appId, function, inputData);
            }
        });
        thread.getId();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void doTask(String appId, FunctionService function, InputData inputData) {
        File workspace = WorkspaceUtil.workspace("infocollection");
        Work work = new Work();
        work.setWorkspace(workspace);
        InfoSet infoSet = null;
        try {
            infoSet = function.work(inputData, work);
        } catch (WorkFailedException e) {
            e.printStackTrace();
        }
        FileUtil.delete(workspace);
        InfoManager.save(appId, infoSet.getInfoList());

    }
}
