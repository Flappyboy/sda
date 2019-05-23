package cn.edu.nju.software.sda.core;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.Info;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoDaoManager {

//    static Map<String, InfoDao> nameInfoDaoMap = new HashMap<>();
    static Map<Class, InfoDao> clazzInfoDaoMap = new HashMap<>();

    static List<InfoDao> infoDaoList = new ArrayList<>();

    public static List<InfoDao> allInfoDaos(){
        return infoDaoList;
    }

    private static void register(InfoDao infoDao){
        for (InfoDao oldInfoDao :
                infoDaoList) {
            if(infoDao.getClass().equals(oldInfoDao.getClass())){
                return;
            }
        }
        infoDaoList.add(infoDao);
    }

   /* public static void register(String infoName, InfoDao infoDao){
        if(StringUtils.isBlank(infoName)){
            throw new RuntimeException("infoName is blank or null");
        }
        nameInfoDaoMap.put(infoName, infoDao);
        register(infoDao);
    }*/

    public static void register(Class clazz, InfoDao infoDao){
        if(clazz == null){
            throw new RuntimeException("clazz is null");
        }
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz is not subclass for Info");
        }
        clazzInfoDaoMap.put(clazz, infoDao);
        register(infoDao);
    }

    public static InfoDao getInfoDao(Class clazz){
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz "+ clazz.getName()+" is not subclass of Info");
        }
        return clazzInfoDaoMap.get(clazz);
    }

    public static InfoDao getInfoDao(String name){
        return clazzInfoDaoMap.get(InfoNameManager.getClassByName(name));
    }

    public static InfoDao getInfoDao(Info info){
        if(info == null) {
            throw new RuntimeException("info is null");
        }
        InfoDao infoDao = null;
//        infoDao = nameInfoDaoMap.get(info.getName());
        if(infoDao == null){
            infoDao = getInfoDao(info.getClass());
        }
        return infoDao;
    }
}
