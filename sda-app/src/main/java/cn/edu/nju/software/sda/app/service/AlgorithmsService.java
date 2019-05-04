package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.Algorithms;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParam;

import java.util.HashMap;
import java.util.List;

public interface AlgorithmsService {

    public void saveAlgorithms(Algorithms algorithms, List<AlgorithmsParam> algorithmsParams);

    public HashMap<String, Object> queryAlgorithmsById(String id);

    public List<Algorithms> queryAlgorithmsListPaged(Integer page, Integer pageSize);

    public void updateAlgorithms(Algorithms algorithms, List<AlgorithmsParam> algorithmsParams);

    public void deleteAlgorithms(String id);

    public int countOfAlgorithms();
}
