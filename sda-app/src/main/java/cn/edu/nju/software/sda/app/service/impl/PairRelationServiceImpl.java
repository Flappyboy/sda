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
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(PairRelationEntity pairRelationEntity) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void statisticsCallInfo(PairRelationInfoEntity pairRelationInfoEntity) {
        ObjectMapper MAPPER = new ObjectMapper();
        AppEntity app = appService.queryAppById(pairRelationInfoEntity.getAppId());
        if(app==null){
            log.error("no app for parentId="+ pairRelationInfoEntity.getAppId());
        }
        List<NodeEntity> nodeEntityList = nodeService.findByAppid(app.getId());
        List<String> classNameList = new ArrayList<>();
        for (NodeEntity node: nodeEntityList) {
            classNameList.add(node.getName());
        }

        Map<String, Object> map = new HashMap<>();

        map.put("an",app.getName());
        try {
            map.put("pnList",MAPPER.writeValueAsString(classNameList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        map.put("analysisId", pairRelationInfoEntity.getId());
        /*if(pairRelationInfoEntity.getStarttine()!=null)
            map.put("startTime", pairRelationInfoEntity.getStarttine().getTime());
        if(pairRelationInfoEntity.getEndtime()!=null)
            map.put("endTime", pairRelationInfoEntity.getEndtime().getTime());*/

        String json = doPost(SdaConfig.getProperty(SdaAppConfig.PINPOINT_URL) +"/statistcsallcall.pinpoint",map);
        log.debug(json);

        try {
            DynamicCallInfoResult result = MAPPER.readValue(json, DynamicCallInfoResult.class);
            List<PairRelationEntity> dynamicCallInfoList = result.getData().getList();
            for (PairRelationEntity dynamicCall :
                    dynamicCallInfoList) {
                save(dynamicCall);
            }
            if (result.getData().getFlag() == 0) {
                PairRelationInfoEntity ainfo = new PairRelationInfoEntity();
                ainfo.setId(pairRelationInfoEntity.getId());
                ainfo.setStatus(1);
                ainfo.setUpdatedAt(new Date());
                System.out.println(ainfo);
                pairRelationInfoMapper.updateByPrimaryKeySelective(ainfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(PairRelationEntity pairRelationEntity) {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String pairRelationEntityId) {

    }


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

    public static String doPost(String url, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(600000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
//            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        PairRelationServiceImpl dynamicCallService = new PairRelationServiceImpl();
        PairRelationInfoEntity pairRelationInfoEntity = new PairRelationInfoEntity();
        pairRelationInfoEntity.setId("1");
        pairRelationInfoEntity.setAppId("190213BH898H4DAW");
        dynamicCallService.statisticsCallInfo(pairRelationInfoEntity);
    }
}
