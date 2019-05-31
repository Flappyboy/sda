package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.config.SdaAppConfig;
import cn.edu.nju.software.sda.app.dao.NodeMapper;
import cn.edu.nju.software.sda.app.dao.PairRelationInfoMapper;
import cn.edu.nju.software.sda.app.dao.PairRelationMapper;
import cn.edu.nju.software.sda.app.dto.DynamicCallInfoResult;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.core.config.SdaConfig;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class PairRelationServiceImpl implements PairRelationService {

    @Autowired
    private AppService appService;

    @Autowired
    private PairRelationMapper pairRelationMapper;
    @Autowired
    private PairRelationInfoMapper pairRelationInfoMapper;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private PairRelationInfoService pairRelationInfoService;


    @Override
    public PairRelationEntity queryCallById(String id) {
        PairRelationEntity pairRelationEntity = pairRelationMapper.selectByPrimaryKey(id);
        pairRelationEntity.setSourceNodeObj(nodeService.findById(pairRelationEntity.getSourceNode()));
        pairRelationEntity.setTargetNodeObj(nodeService.findById(pairRelationEntity.getTargetNode()));
        pairRelationEntity.setInfoName(pairRelationInfoService.queryInfoById(pairRelationEntity.getInfoId()).getName());
        return pairRelationEntity;
    }

    @Override
    public List<PairRelationEntity> findByDynamicAndType(String dynamicAnalysisInfoId, int type) {
        return null;
    }

    @Override
    public List<PairRelationEntity> pairRelationsForNode(String nodeId, List<PairRelationInfoEntity> pairRelationInfoEntities) {
        List<PairRelationEntity> pairRelationEntities = new ArrayList<>();

        for (PairRelationInfoEntity prif :
                pairRelationInfoEntities) {
            Example example = new Example(PairRelationEntity.class);
            PairRelationEntity demo = new PairRelationEntity();
            demo.setInfoId(prif.getId());
            demo.setTargetNode(nodeId);
            example.createCriteria().andEqualTo(demo);
            pairRelationEntities.addAll(pairRelationMapper.selectByExample(example));
        }

       return pairRelationEntities;
    }

    @Override
    public List<PairRelationEntity> findByInfoId(String infoId) {
        Example example = new Example(PairRelationEntity.class);
        PairRelationEntity demo = new PairRelationEntity();
        demo.setFlag(1);
        if(StringUtils.isNotBlank(infoId)){
            demo.setInfoId(infoId);
        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<PairRelationEntity> pairRelationEntities = pairRelationMapper.selectByExample(example);
        return pairRelationEntities;
    }
}
