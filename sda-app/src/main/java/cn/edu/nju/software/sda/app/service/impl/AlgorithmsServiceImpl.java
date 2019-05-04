package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.AlgorithmsMapper;
import cn.edu.nju.software.sda.app.dao.AlgorithmsParamMapper;
import cn.edu.nju.software.sda.app.entity.Algorithms;
import cn.edu.nju.software.sda.app.entity.AlgorithmsExample;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParam;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParamExample;
import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AlgorithmsServiceImpl implements AlgorithmsService {

    @Autowired
    private AlgorithmsMapper algorithmsMapper;
    @Autowired
    private AlgorithmsParamMapper algorithmsParamMapper;
    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAlgorithms(Algorithms algorithms, List<AlgorithmsParam> algorithmsParams) {
        String id = sid.nextShort();
        algorithms.setId(id);
        algorithms.setCreatedat(new Date());
        algorithms.setUpdatedat(new Date());
        algorithms.setFlag(1);
        algorithmsMapper.insert(algorithms);
        for(AlgorithmsParam param :algorithmsParams){
            String paramId = sid.nextShort();
            param.setId(paramId);
            param.setCreatedat(new Date());
            param.setUpdatedat(new Date());
            param.setFlag(1);
            param.setAlgorithmsid(id);
            algorithmsParamMapper.insert(param);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public HashMap<String, Object> queryAlgorithmsById(String id) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Algorithms algorithms = new Algorithms();
        AlgorithmsExample example = new AlgorithmsExample();
        AlgorithmsExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id).andFlagEqualTo(1);
        List<Algorithms> algorithmses = algorithmsMapper.selectByExample(example);
        if (algorithmses.size() > 0 && algorithmses != null)
            algorithms = algorithmses.get(0);
        else
            return null;

        AlgorithmsParamExample paramExample = new AlgorithmsParamExample();
        AlgorithmsParamExample.Criteria paramCriteria = paramExample.createCriteria();
        paramCriteria.andAlgorithmsidEqualTo(id).andFlagEqualTo(1);
        List<AlgorithmsParam> algorithmsParams = algorithmsParamMapper.selectByExample(paramExample);
        result.put("algorithms",algorithms);
        result.put("params",algorithmsParams);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Algorithms> queryAlgorithmsListPaged(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        AlgorithmsExample example = new AlgorithmsExample();
        AlgorithmsExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        List<Algorithms> algorithmses = algorithmsMapper.selectByExample(example);
        return algorithmses;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateAlgorithms(Algorithms algorithms, List<AlgorithmsParam> algorithmsParams) {
        algorithms.setUpdatedat(new Date());
        algorithms.setFlag(1);
        algorithmsMapper.updateByPrimaryKeySelective(algorithms);

        AlgorithmsParamExample paramExample = new AlgorithmsParamExample();
        AlgorithmsParamExample.Criteria paramCriteria = paramExample.createCriteria();
        paramCriteria.andAlgorithmsidEqualTo(algorithms.getId());
        List<AlgorithmsParam> algorithmsOldParams = algorithmsParamMapper.selectByExample(paramExample);
        for(AlgorithmsParam param :algorithmsOldParams){
            AlgorithmsParamExample pdExample = new AlgorithmsParamExample();
            AlgorithmsParamExample.Criteria pdCriteria = pdExample.createCriteria();
            pdCriteria.andAlgorithmsidEqualTo(algorithms.getId()).andFlagEqualTo(1);
            algorithmsParamMapper.deleteByExample(pdExample);
        }

        for(AlgorithmsParam param :algorithmsParams){
            String paramId = sid.nextShort();
            param.setId(paramId);
            param.setCreatedat(new Date());
            param.setUpdatedat(new Date());
            param.setFlag(1);
            param.setAlgorithmsid(algorithms.getId());
            algorithmsParamMapper.insert(param);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAlgorithms(String id) {
        Algorithms algorithms = algorithmsMapper.selectByPrimaryKey(id);
        algorithms.setFlag(0);
        algorithmsMapper.updateByPrimaryKey(algorithms);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfAlgorithms() {
        AlgorithmsExample example = new AlgorithmsExample();
        AlgorithmsExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        return algorithmsMapper.countByExample(example);
    }
}
