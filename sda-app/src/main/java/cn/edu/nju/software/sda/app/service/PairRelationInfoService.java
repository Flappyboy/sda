package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;

import java.util.List;

public interface PairRelationInfoService {

    void updateInfo(PairRelationInfoEntity info);

    void deleteInfo(String infoId);

    PairRelationInfoEntity queryInfoById(String infoId);

    List<PairRelationInfoEntity> queryInfoListPaged(Integer page, Integer pageSize, String appId, String desc);

    int countOfInfo(String appId,String desc);

    //新增接口
    List<PairRelationInfoEntity> queryPairRelationInfoList(String appId, String name);
    List<PairRelationInfoEntity> queryPairRelationInfoByAppId(String appId);

}
