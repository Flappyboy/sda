package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.NodeMapper;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.entity.PartitionDetailEntity;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.core.NodeManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NodeInfoDao implements InfoDao<NodeInfo> {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private NodeService nodeService;

    @Override
    public NodeInfo saveProfile(NodeInfo info) {
        return null;
    }

    @Override
    public NodeInfo saveDetail(NodeInfo info) {
        //Detail
        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setAppId(info.getParentId());
        Example example = new Example(NodeEntity.class);
        example.createCriteria().andEqualTo(nodeEntity);
        List<NodeEntity> nodeEntities = nodeMapper.selectByExample(example);
        Map<String, NodeEntity> nodeEntityMap = NodeEntity.toNameNodeMap(nodeEntities);

        List<Node> nodeListForInsert = new ArrayList<>();
        for (Node node :
                info.getNodeSet()) {
            NodeEntity ne = nodeEntityMap.get(node.getName());
            if(StringUtils.isBlank(node.getId())){
                if (ne == null){
                    node.setId(Sid.nextShort());
                }else{
                    node.setId(ne.getId());
                }
            }
            if(node.getParentNode()!=null){
                Node parentNode = info.getNodeSet().getNodeByName(node.getParentNode().getName());
                if(StringUtils.isBlank(parentNode.getId())){
                    NodeEntity parentNodeEntity = nodeEntityMap.get(parentNode.getName());
                    if(parentNodeEntity == null){
                        parentNode.setId(Sid.nextShort());
                    }else{
                        parentNode.setId(parentNodeEntity.getId());
                    }
                }
                node.setParentNode(parentNode);
            }
            if(ne == null){
                nodeListForInsert.add(node);
            }else{
                NodeEntity newNe = ne.createNodeEntityForUpdate(node);
                if(newNe != null){
                    nodeMapper.updateByPrimaryKeySelective(newNe);
                }
            }
        }
        if(nodeListForInsert.size() > 0)
            nodeMapper.insertList(NodeEntity.createNewEntities(info.getParentId(), nodeListForInsert));
        return info;
    }

    @Override
    public NodeInfo updateProfileInfoById(NodeInfo info) {
        return null;
    }

    @Override
    public NodeInfo deleteById(String infoId) {
        NodeInfo nodeInfo = queryDetailInfoById(infoId);
        nodeService.deleteByAppid(infoId);
        return nodeInfo;
    }

    @Override
    public NodeInfo queryProfileInfoById(String infoId) {
        return null;
    }

    @Override
    public List<NodeInfo> queryProfileInfoByAppId(String appId) {
        return null;
    }

    @Override
    public List<NodeInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        return null;
    }

    @Override
    public NodeInfo queryDetailInfoById(String infoId){
        List<NodeEntity> nodeEntities = nodeService.findByAppid(infoId);

        Map<String, Node> idNodeMap = new HashMap<String,Node>();//id-->node
        Map<Node, String> nodePidMap = new HashMap<Node,String>();//node-->pid
        Map<String,NodeSet> pidNodeMap = new HashMap<>();//pid-->node

        NodeSet nodeSet = new NodeSet();

        for(NodeEntity nodeEntity:nodeEntities){
            Node node = nodeEntity.toNode();
//            System.out.println(nodeEntity.getParentNode());
            if(nodeEntity.getParentNode()!=null){
                String  pid = nodeEntity.getParentNode();
                if (pidNodeMap.containsKey(pid)) {
                    NodeSet nodechilds = pidNodeMap.get(pid);
                    nodechilds.addNode(node);
                    pidNodeMap.put(pid, nodechilds);
                } else {
                    NodeSet nodechilds = new NodeSet();
                    nodechilds.addNode(node);
                    pidNodeMap.put(pid, nodechilds);
                }
            }
            idNodeMap.put(nodeEntity.getId(),node);
            nodePidMap.put(node,nodeEntity.getParentNode());
            nodeSet.addNode(node);
        }

        for (Map.Entry<Node, String> entry : nodePidMap.entrySet()) {
            String pid =entry.getValue();
            Node node = entry.getKey();
            node.setParentNode(idNodeMap.get(pid));
            node.setChildrenNodeSet(pidNodeMap.get(node.getId()));
//            nodeSet.addNode(node);

        }
        NodeInfo nodeInfo = new NodeInfo(nodeSet);
        return nodeInfo;
    }
}
