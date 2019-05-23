package cn.edu.nju.software.sda.core.dao;

import cn.edu.nju.software.sda.core.domain.info.Info;

import java.util.List;

public interface InfoDao<I extends Info> {
    I save(I info);

    I updateById(I info);

    I deleteById(I info);

    List<I> querySimpleInfoByAppId(String appId);

    List<I> querySimpleInfoByAppId(String appId, Integer pageSize, Integer page);

    Integer countOfQuerySimpleInfoByAppId(String appId);

    I queryDetailInfoById(String infoId);
}
