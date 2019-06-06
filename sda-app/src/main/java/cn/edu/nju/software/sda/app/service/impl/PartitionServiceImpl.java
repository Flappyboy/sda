package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.Task.Task;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
public class PartitionServiceImpl implements PartitionService {
    @Autowired
    private PartitionInfoMapper partitionMapper;

    @Autowired
    private PairRelationInfoService pairRelationInfoService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private PartitionPairMapper partitionPairMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private AppService appService;

    @Autowired
    private EvaluationInfoService evaluationInfoService;

    @Override
    public PartitionInfoEntity findPartitionById(String partitionId) {
        PartitionInfoEntity partitionInfoEntity = partitionMapper.selectByPrimaryKey(partitionId);
        Example example = new Example(PartitionPairEntity.class);
        PartitionPairEntity partitionPairEntity = new PartitionPairEntity();
        partitionPairEntity.setPartitionInfoId(partitionId);
        example.createCriteria().andEqualTo(partitionPairEntity);
        List<PartitionPairEntity> partitionPairEntities = partitionPairMapper.selectByExample(example);
        List<PairRelationInfoEntity> pairRelationInfoEntities = new ArrayList<>();
        for (PartitionPairEntity ppe :
                partitionPairEntities) {
            PairRelationInfoEntity prie = pairRelationInfoService.queryInfoById(ppe.getPairRelationInfoId());
            if(prie != null){
                pairRelationInfoEntities.add(prie);
            }
        }
        partitionInfoEntity.setPairRelationInfoEntityList(pairRelationInfoEntities);
        return partitionInfoEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionInfoEntity savePartition(PartitionInfoEntity partition) {
        String id = Sid.nextShort();
        partition.setId(id);
        partition.setCreatedAt(new Date());
        partition.setUpdatedAt(new Date());
        partition.setFlag(1);
        partition.setStatus(Info.InfoStatus.SAVING.name());
        partitionMapper.insertSelective(partition);
        return partition;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delPartition(String id) {
        PartitionInfoEntity partition = new PartitionInfoEntity();
        partition.setId(id);
        partition.setFlag(0);
        updatePartition(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionInfoEntity updatePartition(PartitionInfoEntity partition) {
        if(partition == null){
            return null;
        }
        partition.setUpdatedAt(new Date());
        partitionMapper.updateByPrimaryKeySelective(partition);

        PartitionInfoEntity pife = partitionMapper.selectByPrimaryKey(partition.getId());
        pife.setAppName(appService.queryAppById(pife.getAppId()).getName());
        pife.setTaskEntity(taskService.queryTaskByOutputInfoId(pife.getId()));
        return pife;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PageQueryDto<PartitionInfoEntity> queryPartitionInfoPaged(@NotNull PageQueryDto<PartitionInfoEntity> dto, PartitionInfoEntity partitionInfoEntity) {
        // 开始分页
        Page p = PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), true);

        Example example = new Example(PartitionInfoEntity.class);
        partitionInfoEntity.setFlag(1);
        example.createCriteria().andEqualTo(partitionInfoEntity);
        example.setOrderByClause("created_at desc");
        List<PartitionInfoEntity> partitionList = partitionMapper.selectByExample(example);
        for (PartitionInfoEntity pife :
                partitionList) {
            pife.setAppName(appService.queryAppById(pife.getAppId()).getName());
            pife.setTaskEntity(taskService.queryTaskByOutputInfoId(pife.getId()));
        }
        return dto.addResult(partitionList, p.getTotal());
    }

    @Override
    public PartitionInfoEntity copyByInfoId(String partitionInfoId) {
        PartitionInfoEntity pif = findPartitionById(partitionInfoId);
        if(pif == null){
            return null;
        }
        pif.setId(null);
        pif.setDesc("Copy from " + partitionInfoId);
        PartitionInfoEntity newPif = savePartition(pif);
        List<PartitionNodeEntity> nodeEntities = partitionNodeService.queryPartitionResult(partitionInfoId);
        for (PartitionNodeEntity nodeEntity :
                nodeEntities) {
            List<PartitionDetailEntity> detailEntities = partitionDetailService.queryPartitionDetailListByPartitionNodeId(nodeEntity.getId());
            nodeEntity.setId(null);
            nodeEntity.setPartitionId(newPif.getId());
            PartitionNodeEntity newNodeEntity = partitionNodeService.savePartitionNode(nodeEntity);
            for (PartitionDetailEntity detailEntity :
                    detailEntities) {
                detailEntity.setId(null);
                detailEntity.setPartitionNodeId(newNodeEntity.getId());
                partitionDetailService.savePartitionDetail(detailEntity);
            }
        }

        Example example = new Example(PartitionPairEntity.class);
        PartitionPairEntity partitionPairEntity = new PartitionPairEntity();
        partitionPairEntity.setPartitionInfoId(partitionInfoId);
        example.createCriteria().andEqualTo(partitionPairEntity);
        List<PartitionPairEntity> partitionPairEntities = partitionPairMapper.selectByExample(example);
        for (PartitionPairEntity pairEntity :
                partitionPairEntities) {
            pairEntity.setId(Sid.nextShort());
            pairEntity.setPartitionInfoId(newPif.getId());
        }
        partitionPairMapper.insertList(partitionPairEntities);

        partitionNodeEdgeService.statisticsPartitionResultEdge(newPif.getId());

        EvaluationInfoEntity evaluationInfoEntity = evaluationInfoService.queryLastEvaluationByPartitionId(partitionInfoId);

        String taskEntityId = evaluationInfoService.redo(evaluationInfoEntity.getId());
        TaskEntity taskEntity;
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskEntity = taskService.queryTaskEntityById(taskEntityId);
            if(!taskEntity.getStatus().equals(Task.Status.Doing.name()) && taskEntity.getTaskDataList().size()>0){
                break;
            }
        }
        taskEntity.setId(null);
        List<TaskDataEntity> dataEntities = taskEntity.getTaskDataList();
        taskEntity.setInputDataDto(null);
        for (TaskDataEntity dataEntity :
                dataEntities) {
            dataEntity.setId(null);
            if (dataEntity.getDataType().equals(Partition.INFO_NAME_PARTITION)) {
                dataEntity.setData(newPif.getId());
            }
        }
        taskEntityId = taskService.newTask(taskEntity).getId();

        while (true){
            taskEntity = taskService.queryTaskEntityById(taskEntityId);
            if(!taskEntity.getStatus().equals(Task.Status.Doing.name())){
                break;
            }
        }
        return pif;
    }

}
