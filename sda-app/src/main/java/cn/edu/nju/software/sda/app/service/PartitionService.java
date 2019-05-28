package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import javax.validation.constraints.NotNull;

public interface PartitionService {

    PartitionInfoEntity findPartitionById(String partitionId);

    PartitionInfoEntity savePartition(PartitionInfoEntity partition);

    void delPartition(String partitionInfoId);

    void updatePartition(PartitionInfoEntity partition);

    PageQueryDto<PartitionInfoEntity> queryPartitionInfoPaged(@NotNull PageQueryDto<PartitionInfoEntity> dto, PartitionInfoEntity partitionInfoEntity);

//    PartitionGraph getGraph(String partitionInfoId);
}
