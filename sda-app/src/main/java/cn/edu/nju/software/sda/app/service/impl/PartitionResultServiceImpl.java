package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.git.GitDataUtil;
import cn.edu.nju.software.git.GitUtil;
import cn.edu.nju.software.git.entity.GitCommitFileEdge;
import cn.edu.nju.software.git.entity.GitCommitRetn;
import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.entity.bean.EdgeBean;
import cn.edu.nju.software.sda.app.mock.dto.EdgeDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.app.utils.FileUtil;
import cn.edu.nju.software.sda.app.utils.louvain.LouvainUtil;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class PartitionResultServiceImpl implements PartitionResultService {
    @Value("${filepath}")
    private String path;

    @Autowired
    private PartitionResultMapper partitionResultMapper;

    @Autowired
    private PartitionInfoMapper partitionInfoMapper;

    @Autowired
    private StaticCallService staticCallService;

    @Autowired
    private DynamicCallService dynamicCallService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionResultEdgeService partitionResultEdgeService;

    @Autowired
    private ClassNodeService classNodeService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionResult savePartitionResult(PartitionResult partitionResult) {
        String id = sid.nextShort();
        partitionResult.setId(id);
        partitionResult.setCreatedAt(new Date());
        partitionResult.setUpdatedAt(new Date());
        partitionResult.setFlag(1);
        partitionResultMapper.insert(partitionResult);
        return partitionResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionResult(PartitionResult partitionResult) {
        partitionResult.setUpdatedAt(new Date());
        partitionResultMapper.updateByPrimaryKeySelective(partitionResult);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionResult(String partitionResultId) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setId(partitionResultId);
        partitionResult.setFlag(0);
        partitionResult.setUpdatedAt(new Date());

        partitionResultMapper.updateByPrimaryKeySelective(partitionResult);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PartitionResult queryPartitionResultById(String partitionResultId) {
        PartitionResult partitionResult = partitionResultMapper.selectByPrimaryKey(partitionResultId);
        if(partitionResult == null || partitionResult.getFlag().equals(1))
            partitionResult = null;
        return partitionResult;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<PartitionResult> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionResult.setAlgorithmsId(algorithmsId);
        partitionResult.setType(type);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
//        example.setOrderByClause("createdAt");

        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);
        return partitionResultList;
    }

    @Override
    public GraphDto queryPartitionResultForDto(String partitionId) {
        GraphDto graphDto = new GraphDto();

        List<PartitionResult> partitionResults = queryPartitionResult(partitionId);
        for (PartitionResult p : partitionResults) {
            NodeDto nodeDto = new NodeDto();
            int count = partitionDetailService.countOfPartitionDetailByResultId(p.getId());
            nodeDto.setId(p.getId());
            nodeDto.setName(p.getName());
            nodeDto.setSize(count);
            graphDto.addNode(nodeDto);
        }
        List<PartitionResultEdge> partitionResultEdgeList = partitionResultEdgeService.findPartitionResultEdge(partitionId);
        for (PartitionResultEdge p : partitionResultEdgeList) {
            EdgeDto edgeDto = new EdgeDto();
            edgeDto.setId(p.getId());
            edgeDto.setCount(partitionResultEdgeService.countOfPartitionResultEdgeCallByEdgeId(p.getId()));
            edgeDto.setSource(p.getPatitionResultAId());
            edgeDto.setTarget(p.getPatitionResultBId());
            graphDto.addEdge(edgeDto);
        }
        return graphDto;
    }

    @Override
    public List<PartitionResult> queryPartitionResult(String partitionId) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setPartitionId(partitionId);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);
        return partitionResultList;
    }

    @Override
    public List<String> findPartitionResultIds(String partitionId) {
        List<PartitionResult> partitionResultList = queryPartitionResult(partitionId);
        List<String> idList = new ArrayList<>();
        for (PartitionResult pr :
                partitionResultList) {
            idList.add(pr.getId());
        }
        return idList;
    }

    @Override
    public PartitionResult queryPartitionResult(String partitionId, String partitionResultName) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setPartitionId(partitionId);
        partitionResult.setName(partitionResultName);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);

        if(partitionResultList == null || partitionResultList.size() <= 0)
            return null;
        return partitionResultList.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void partition(PartitionInfo partitionInfo) throws Exception {
        String appid = partitionInfo.getAppid();
        String algorithmsid = partitionInfo.getAlgorithmsid();
        String dynamicanalysisinfoid = partitionInfo.getDynamicanalysisinfoid();
        int type = partitionInfo.getType();
        String partitionId = partitionInfo.getId();
        //获取静态数据，并标号


        List<StaticCallInfo> staticCallInfos = staticCallService.findByAppIdAndType(appid, type);
        HashMap<String, Integer> nodeKeys = new HashMap<String, Integer>();
        HashMap<Integer, String> keyNodes = new HashMap<Integer, String>();
        int key = 0;
        for (StaticCallInfo staticCallInfo : staticCallInfos) {
            if (nodeKeys.get(staticCallInfo.getCaller()) == null) {
                key += 1;
                nodeKeys.put(staticCallInfo.getCaller(), key);
                keyNodes.put(key, staticCallInfo.getCaller());
            }
            if (nodeKeys.get(staticCallInfo.getCallee()) == null) {
                key += 1;
                nodeKeys.put(staticCallInfo.getCallee(), key);
                keyNodes.put(key, staticCallInfo.getCallee());
            }
        }

        //获取动态数据，补充标号
        List<DynamicCallInfo> dynamicCallInfos = new ArrayList<>();
        if (dynamicanalysisinfoid != null) {
            dynamicCallInfos = dynamicCallService.findByDynamicAndType(dynamicanalysisinfoid, type);
            for (DynamicCallInfo dynamicCallInfo : dynamicCallInfos) {
                if (nodeKeys.get(dynamicCallInfo.getCaller()) == null) {
                    key += 1;
                    nodeKeys.put(dynamicCallInfo.getCaller(), key);
                }
                if (nodeKeys.get(dynamicCallInfo.getCallee()) == null) {
                    key += 1;
                    nodeKeys.put(dynamicCallInfo.getCallee(), key);
                }
            }
        }

        //静态数据边标号
        HashMap<String, EdgeBean> edges = new HashMap<String, EdgeBean>();
        for (StaticCallInfo staticCallInfo : staticCallInfos) {
            String edgeKey = staticCallInfo.getCaller() + "_" + staticCallInfo.getCallee();
            EdgeBean edge = new EdgeBean();
            edge.setSourceId(staticCallInfo.getCaller());
            int sourceKey = nodeKeys.get(staticCallInfo.getCaller());
            edge.setSourceKey(sourceKey);
            edge.setTargetId(staticCallInfo.getCallee());
            int targetKey = nodeKeys.get(staticCallInfo.getCallee());
            edge.setTargetKey(targetKey);
            edge.setWeight(staticCallInfo.getCount());
            edges.put(edgeKey, edge);
        }

        //动态数据边标号
        if (dynamicCallInfos != null && dynamicCallInfos.size() > 0) {
            for (DynamicCallInfo dynamicCallInfo : dynamicCallInfos) {
                String edgeKey = dynamicCallInfo.getCaller() + "_" + dynamicCallInfo.getCallee();
                EdgeBean edge = edges.get(edgeKey);
                if (edge == null) {
                    EdgeBean newedge = new EdgeBean();
                    newedge.setSourceId(dynamicCallInfo.getCaller());
                    int sourceKey = nodeKeys.get(dynamicCallInfo.getCaller());
                    newedge.setSourceKey(sourceKey);
                    newedge.setTargetId(dynamicCallInfo.getCallee());
                    int targetKey = nodeKeys.get(dynamicCallInfo.getCallee());
                    newedge.setTargetKey(targetKey);
                    newedge.setWeight(dynamicCallInfo.getCount());
                    edges.put(edgeKey, newedge);
                } else {
                    int dyCount = dynamicCallInfo.getCount();
                    int stCount = edge.getWeight();
                    edge.setWeight(dyCount + stCount);
                    edges.put(edgeKey, edge);
                }
            }
        }

        //生成算法处理的输入文件
//        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String edgePath = path + "/partition/";
        String edgeFileName = System.currentTimeMillis() + "_edge.txt";
        FileUtil.creatFile(edgePath, edgeFileName);
        String filePath = edgePath + edgeFileName;
        List<String> lines = new ArrayList<>();
        String count = (nodeKeys.size() + 1) + " " + edges.size();
        lines.add(count);
        for (Map.Entry<String, EdgeBean> entry : edges.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            EdgeBean edge = entry.getValue();
            String line = edge.getSourceKey() + " " + edge.getTargetKey() + " " + edge.getWeight();
            lines.add(line);
        }
        FileUtil.writeFile(lines, filePath);
        String outputPath = edgePath + System.currentTimeMillis() + "_louvain.txt";
        LouvainUtil.execute(filePath, outputPath);

        //划分结果入库
        List<String> resultLines = FileUtil.readFile(outputPath);
        int communityCount = 0;
        for (int j = 1; j < resultLines.size(); j++) {
//            if(!resultLine.trim().endsWith("0")&&resultLine.trim()!="0"){
            String resultLine = resultLines.get(j);
            System.out.println(resultLine);
            communityCount += 1;
            PartitionResult partitionResult = new PartitionResult();
            partitionResult.setAlgorithmsId(algorithmsid);
            partitionResult.setAppId(appid);
            partitionResult.setDesc("community: " + communityCount);
            partitionResult.setDynamicAnalysisInfoId(dynamicanalysisinfoid);
            partitionResult.setName(String.valueOf(communityCount));
            partitionResult.setOrder(communityCount);
            partitionResult.setType(type);
            partitionResult.setPartitionId(partitionId);
            PartitionResult pr = savePartitionResult(partitionResult);

            String[] communityKeys = resultLine.split(" ");
            for (int i = 0; i < communityKeys.length; i++) {
                String nodeId = keyNodes.get(Integer.valueOf(communityKeys[i]));
                PartitionDetail partitionDetail = new PartitionDetail();
                partitionDetail.setDesc(pr.getDesc() + "的结点");
                partitionDetail.setNodeId(nodeId);
                partitionDetail.setPatitionResultId(pr.getId());
                partitionDetail.setType(type);
                partitionDetailService.savePartitionDetail(partitionDetail);
            }

//            }

        }
        FileUtil.delete(outputPath);
        FileUtil.delete(filePath);

        partitionResultEdgeService.statisticsPartitionResultEdge(partitionInfo);

        PartitionInfo newPartitionInfo = new PartitionInfo();
        newPartitionInfo.setId(partitionId);
        newPartitionInfo.setStatus(1);
        partitionInfoMapper.updateByPrimaryKeySelective(newPartitionInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfPartitionResult(String dynamicInfoId, String algorithmsId, int type) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setAlgorithmsId(algorithmsId);
        partitionResult.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionResult.setType(type);
        partitionResult.setFlag(1);
        return partitionResultMapper.selectCount(partitionResult);
    }

    @Override
    public int countOfPartitionResult(String id) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setPartitionId(id);
        partitionResult.setFlag(1);
        return partitionResultMapper.selectCount(partitionResult);
    }

    // 结合gitcommit信息
    private HashMap<String, EdgeBean> mergeCommitEdge(String gitPath,HashMap<String, EdgeBean> edges,HashMap<String, Integer> nodeKeys,String appId,int maxKey)throws Exception{
        GitCommitRetn gitCommitRetn = GitUtil.getGitCommitInfo(1,gitPath);
        Map<String, GitCommitFileEdge> gitCommitFileEdgeMap = GitDataUtil.getCommitFileGraph(gitCommitRetn);
        for (Map.Entry<String, GitCommitFileEdge> entry : gitCommitFileEdgeMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            GitCommitFileEdge gitCommitFileEdge =  entry.getValue();
            List<ClassNode> sourceClassNodes = classNodeService.findBycondition(gitCommitFileEdge.getSourceName(),appId);
            List<ClassNode> targetClassNodes = classNodeService.findBycondition(gitCommitFileEdge.getTargetName(),appId);
            String sourceId = sourceClassNodes.get(0).getId();
            String targetId =targetClassNodes.get(0).getId();
            int sourceKey =  nodeKeys.get(sourceId);
            int targetKey =  nodeKeys.get(targetId);

            String edgeKey = sourceId + "_" + targetId;
            EdgeBean edge = edges.get(edgeKey);
            if (edge == null) {
                EdgeBean newedge = new EdgeBean();
                newedge.setSourceId(sourceId);
                newedge.setSourceKey(sourceKey);
                newedge.setTargetId(targetId);
                newedge.setTargetKey(targetKey);
                newedge.setWeight(gitCommitFileEdge.getCount());
                edges.put(edgeKey, newedge);
            } else {
                int gitCount = gitCommitFileEdge.getCount();
                int stCount = edge.getWeight();
                edge.setWeight(gitCount + stCount);
                edges.put(edgeKey, edge);
            }

        }
      return edges;
    }

}
