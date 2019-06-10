package cn.edu.nju.software.sda.plugin.function.info.impl.pinpointdynamicjava;

import cn.edu.nju.software.sda.core.config.SdaConfig;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
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

import java.io.*;
import java.util.*;

public class PinpointDynamicJavaInfoCollection extends InfoCollection {

    public static final String IP = "Pinpoint Ip";
    public static final String PORT = "Pinpoint Port";
    public static final String NAME = "App Name";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem(IP, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(PORT, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(NAME, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) throws WorkFailedException {
        Map<String, List<Object>> dataMap = inputData.getFormDataObjs(getMetaData());
        String appName = (String) dataMap.get(NAME).get(0);
        String ip = (String) dataMap.get(IP).get(0);
        String port = (String) dataMap.get(PORT).get(0);

        Set<ClassNode> set = inputData.getInfoSet().getInfoByClass(NodeInfo.class).getNodeSet().getSetByClass(ClassNode.class);
        List<String> classNameList = new ArrayList<>();
        for (ClassNode node :
                set) {
            classNameList.add(node.getName());
        }


        Map<String, Object> map = new HashMap<>();

        map.put("an",appName);
        try {
            map.put("pnList",MAPPER.writeValueAsString(classNameList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        map.put("analysisId", pairRelationInfoEntity.getId());
        /*if(pairRelationInfoEntity.getStarttine()!=null)
            map.put("startTime", pairRelationInfoEntity.getStarttine().getTime());
        if(pairRelationInfoEntity.getEndtime()!=null)
            map.put("endTime", pairRelationInfoEntity.getEndtime().getTime());*/

        String json = doPost("http://" + ip + ":" + port +"/statistcsallcall.pinpoint",map);
        System.out.println(json);
        PairRelationInfo pairRelationInfo = new PairRelationInfo(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT);

        try {
            DynamicCallInfoResult result = MAPPER.readValue(json, DynamicCallInfoResult.class);
            List<DynamicCallInfo> dynamicCallInfoList = result.getData().getList();
            for (DynamicCallInfo dynamicCall :
                    dynamicCallInfoList) {
                ClassNode sourceNode = new ClassNode(dynamicCall.getCaller());
                ClassNode targetNode = new ClassNode(dynamicCall.getCallee());
                PairRelation pairRelation = new PairRelation(dynamicCall.getCount().doubleValue(), sourceNode, targetNode);
                pairRelationInfo.addRelationByAddValue(pairRelation);
            }
            return new InfoSet(pairRelationInfo);
        } catch (IOException e) {
            throw new WorkFailedException("处理数据失败："+json);
        }
    }

    @Override
    public String getName() {
        return "sys_DynamicJavaFromPinpointInfoCollection";
    }

    @Override
    public String getDesc() {
        return "Collect java program dynamic data from pinpoint.";
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
}
