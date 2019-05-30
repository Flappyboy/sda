package cn.edu.nju.software.sda.core.dao;

import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoManager {

    public static Info queryInfoByNameAndId(String infoName, String infoId){
        return InfoDaoManager.getInfoDao(infoName).queryDetailInfoById(infoId);
    }

    public static List<Info> queryInfoByAppIdAndName(String appId, String name){
        InfoDao infoDao = InfoDaoManager.getInfoDao(name);
        if(infoDao == null){
            return new ArrayList<>();
        }
        return infoDao.queryProfileInfoByAppIdAndInfoName(appId, name);
    }

    private static InfoDao getAvailableInfoDao(Info info){
        return InfoDaoManager.getInfoDao(info);
    }

    public static List<Info> save(String parentId, List<Info> infos) {
        List<Info> objList = new ArrayList<>();
        for (Info info :
                infos) {
            if(info.getClass().equals(NodeInfo.class)) {
                info.setParentId(parentId);
                objList.add(save(info));
            }
        }
        for (Info info :
                infos) {
            if(!info.getClass().equals(NodeInfo.class)) {
                info.setParentId(parentId);
                objList.add(save(info));
            }
        }
        return objList;
    }

    public static Info save(Info info) {
        info.setId(Sid.nextShort());
        info.setStatus(Info.InfoStatus.SAVING);
        info.setCreatedAt(new Date());
        info.setUpdatedAt(new Date());
        getAvailableInfoDao(info).saveProfile(info);
        return getAvailableInfoDao(info).saveDetail(info);
    }

    public static Info updateById(Info info) {
        if(StringUtils.isBlank(info.getId())){
            throw new RuntimeException("info id is blank or null!");
        }
        return getAvailableInfoDao(info).updateProfileInfoById(info);
    }

    public static Info deleteById(Info info) {
        if(StringUtils.isBlank(info.getId())){
            throw new RuntimeException("info id is blank or null!");
        }
        return getAvailableInfoDao(info).deleteById(info.getId());
    }

    public static List<Info> querySimpleInfoByApp(String appId) {
        List<InfoDao> infoDaoList = InfoDaoManager.allInfoDaos();
        List<Info> infoList = new ArrayList<>();
        for(InfoDao infoDao: infoDaoList){
            infoList.addAll(infoDao.queryProfileInfoByAppId(appId));
        }
        return infoList;
    }
}
