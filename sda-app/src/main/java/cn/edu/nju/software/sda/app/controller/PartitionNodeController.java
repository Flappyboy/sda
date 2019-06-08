package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.mock.dto.CallDto;
import cn.edu.nju.software.sda.app.mock.dto.ClassDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "划分结果接口")
@RequestMapping(value = "/api")
public class PartitionNodeController {
    @Autowired
    private PartitionNodeService partitionNodeService;
    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Autowired
    private EvaluationInfoService evaluationInfoService;

    @Autowired
    private TaskService taskService;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "dynamicInfoId", value = "动态信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "algorithmsId", value = "算法id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "结点类型，0-类结点，1-方法结点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "划分结果分页列表", notes = "返回状态200成功")
    @RequestMapping(value = "/partitionResult", method = RequestMethod.GET)
    public JSONResult findPartitionResultList(String dynamicInfoId, String algorithmsId,int type, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        List<PartitionNodeEntity> partitionNodeEntities = partitionNodeService.queryPartitionResultListPaged(dynamicInfoId,algorithmsId, type, page, pageSize);
        int count = partitionNodeService.countOfPartitionResult(dynamicInfoId,algorithmsId, type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list", partitionNodeEntities);
        result.put("total",count);
        return JSONResult.ok(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "partitionId", value = "划分结果id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "结点类型，0-类结点，1-方法结点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "划分结果详情分页列表", notes = "返回状态200成功")
    @RequestMapping(value = "/partitionResult/{id}", method = RequestMethod.GET)
    public JSONResult findPartitionResultDetail(@PathVariable String id, int type, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        List<HashMap<String, String>> partitionDetails = partitionDetailService.queryPartitionDetailListPaged(id, type, page, pageSize);
        int count = partitionDetailService.countOfPartitionDetail(id, type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",partitionDetails);
        result.put("total",count);
        return JSONResult.ok(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "parentId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "algorithmsId", value = "算法id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "dynamicAnalysisInfoId", value = "动态调用信息id",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "type", value = "类型 0-类结点，1-方法结点",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "partitionId", value = "划分信息id",  dataType = "int")
    })
    @ApiOperation(value = "执行划分", notes = "返回状态200成功")
    @RequestMapping(value = "/partitionResult/do", method = RequestMethod.GET)
    public JSONResult doPartition(String appid,String algorithmsid,String dynamicanalysisinfoid,int type,String partitionId) throws Exception {
        //partitionNodeService.partition(parentId,algorithmsId,dynamicAnalysisInfoId,type,partitionId);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/partition-detail/{id}", method = RequestMethod.GET)
    public JSONResult partitionResultDetail(@PathVariable String id) throws Exception {
        GraphDto graphDto = partitionNodeService.queryPartitionResultForDto(id);
        return JSONResult.ok(graphDto);
    }

    @RequestMapping(value = "/partition-detail-node/{id}", method = RequestMethod.GET)
    public ResponseEntity partitionResultDetailNodes(@PathVariable String id, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageQueryDto dto = partitionDetailService.queryPartitionDetailByResultIdPaged(PageQueryDto.create(page, pageSize), id);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/partition-results/moveNode/{partitionInfoId}", method = RequestMethod.PUT)
    public JSONResult partitionMoveResultDetailNode(@PathVariable String partitionInfoId, String nodeId, String  oldPartitionResultName, String targetPartitionResultName) {

        PartitionGraphOperateDto dto = partitionDetailService.moveNodeToPartition(Arrays.asList(nodeId),
                partitionNodeService.queryPartitionResult(partitionInfoId, oldPartitionResultName),
                partitionNodeService.queryPartitionResult(partitionInfoId, targetPartitionResultName));

        evaluationInfoService.redoLastEvaluationForPartitionId(partitionInfoId);
        return JSONResult.ok(dto);
    }
    @RequestMapping(value = "/partition-results/moveNode/{partitionInfoId}", method = RequestMethod.POST)
    public JSONResult partitionMoveResultDetailNodes(@PathVariable String partitionInfoId, @RequestBody List<String> nodeIds, String  oldPartitionResultName, String targetPartitionResultName) {

        PartitionGraphOperateDto dto = partitionDetailService.moveNodeToPartition(nodeIds,
                partitionNodeService.queryPartitionResult(partitionInfoId, oldPartitionResultName),
                partitionNodeService.queryPartitionResult(partitionInfoId, targetPartitionResultName));

        evaluationInfoService.redoLastEvaluationForPartitionId(partitionInfoId);
        return JSONResult.ok(dto);
    }

    @RequestMapping(value = "/partition-results/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delPartitionNode(@PathVariable String id) {
        partitionNodeService.deletePartitionNode(id);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/partition-results", method = RequestMethod.POST)
    public ResponseEntity addPartitionNode(@RequestBody PartitionNodeEntity partitionNode) {
        partitionNode = partitionNodeService.savePartitionNode(partitionNode);

        return ResponseEntity.ok(partitionNode);
    }

    @RequestMapping(value = "/partition-results", method = RequestMethod.PUT)
    public ResponseEntity putPartitionNode(@RequestBody PartitionNodeEntity partitionNode) {
        partitionNodeService.updatePartitionNode(partitionNode);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/partition-detail-edge/{id}", method = RequestMethod.GET)
    public ResponseEntity partitionResultDetailEdges(@PathVariable String id, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageQueryDto<PartitionNodeEdgePairEntity> dto = partitionNodeEdgeService.findPartitionNodeEdgeCallByEdgeId(PageQueryDto.create(page, pageSize), id);
        return ResponseEntity.ok(dto);
    }

    private List<CallDto> wrapEdges(List<PartitionNodeEdgePairEntity> edges){
        List<CallDto> list = new ArrayList<>();
        for (PartitionNodeEdgePairEntity edge:edges) {
            CallDto callDto = new CallDto();
            callDto.setId(edge.getId());
            PairRelationEntity pair = edge.getPair();
            if(pair != null){
                NodeEntity caller = pair.getSourceNodeObj();
                callDto.setCaller(wrapCallObj(caller));
                NodeEntity callee = pair.getTargetNodeObj();
                callDto.setCallee(wrapCallObj(callee));
            }
            list.add(callDto);
        }
        return list;
    }

    private Object wrapCallObj(Object callObj){
        if (callObj instanceof NodeEntity){
            NodeEntity callerClass = (NodeEntity) callObj;
            ClassDto classDto = new ClassDto();
            classDto.setId(callerClass.getId());
            classDto.setName(callerClass.getName());
            return classDto;
        }else{
            log.error("sourceNode class type wrong");
        }
        return null;
    }
}
