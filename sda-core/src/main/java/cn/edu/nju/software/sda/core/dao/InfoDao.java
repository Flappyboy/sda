package cn.edu.nju.software.sda.core.dao;

import cn.edu.nju.software.sda.core.domain.info.Info;

import java.util.List;

public interface InfoDao<I extends Info> {

    /**
     * 存储Info Info已经赋予了Id
     * @param info
     * @return
     */
    I saveProfile(I info);

    /**
     * 存储Info Info的Profile部分已经被存储
     * @param info
     * @return
     */
    I saveDetail(I info);

    /**
     * 根据Id更新Info的概要信息，不包括详细数据
     * @param info
     * @return
     */
    I updateProfileInfoById(I info);

    /**
     * 根据Id删除Info
     * @param infoId
     * @return
     */
    I deleteById(String infoId);

    /**
     * 查询Info的概要信息
     * @param infoId
     * @return
     */
    I queryProfileInfoById(String infoId);

    List<I> queryProfileInfoByAppId(String appId);

    /**
     * 根据appId和infoName查询 info概要信息
     * 如果是nodeInfo infoName将没有意义
     * @param appId
     * @param infoName
     * @return
     */
    List<I> queryProfileInfoByAppIdAndInfoName(String appId, String infoName);

    /**
     * 查询Info具体数据，需要将Info中的数据进行填充
     * @param infoId
     * @return
     */
    I queryDetailInfoById(String infoId);
}
