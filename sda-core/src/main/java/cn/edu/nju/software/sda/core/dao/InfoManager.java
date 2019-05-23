package cn.edu.nju.software.sda.core.dao;

import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;

import java.util.ArrayList;
import java.util.List;

public class InfoManager {

    public static Info queryInfoByNameAndId(String infoName, String infoId){
        return InfoDaoManager.getInfoDao(infoName).queryDetailInfoById(infoId);
    }

    private static InfoDao getAvailableInfoDao(Info info){
        return InfoDaoManager.getInfoDao(info);
    }

    public static List<Info> save(String appId, List<Info> infos) {
        List<Info> objList = new ArrayList<>();
        for (Info info :
                infos) {
            if(info.getClass().equals(NodeInfo.class)) {
                info.setAppId(appId);
                objList.add(save(info));
            }
        }
        for (Info info :
                infos) {
            if(!info.getClass().equals(NodeInfo.class)) {
                info.setAppId(appId);
                objList.add(save(info));
            }
        }
        return objList;
    }

    public static Info save(Info info) {
        info.setId(Sid.nextShort());
        return getAvailableInfoDao(info).save(info);
    }

    public static Info updateById(Info info) {
        if(StringUtils.isBlank(info.getId())){
            throw new RuntimeException("info id is blank or null!");
        }
        return getAvailableInfoDao(info).updateById(info);
    }

    public static Info deleteById(Info info) {
        if(StringUtils.isBlank(info.getId())){
            throw new RuntimeException("info id is blank or null!");
        }
        return getAvailableInfoDao(info).deleteById(info);
    }

    public static List<Info> querySimpleInfoByApp(String appId) {
        List<InfoDao> infoDaoList = InfoDaoManager.allInfoDaos();
        List<Info> infoList = new ArrayList<>();
        for(InfoDao infoDao: infoDaoList){
            infoList.addAll(infoDao.querySimpleInfoByAppId(appId));
        }
        return infoList;
    }
}
