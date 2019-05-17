package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.mock.dto.CallDto;
import cn.edu.nju.software.sda.app.mock.dto.ClassDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "划分结果接口")
@RequestMapping(value = "/api")
public class PartitionResultController {
    @Autowired
    private PartitionResultService partitionResultService;
    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionResultEdgeService partitionResultEdgeService;

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
        List<PartitionResult> partitionResults = partitionResultService.queryPartitionResultListPaged(dynamicInfoId,algorithmsId, type, page, pageSize);
        int count = partitionResultService.countOfPartitionResult(dynamicInfoId,algorithmsId, type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",partitionResults);
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
            @ApiImplicitParam(paramType="query", name = "appId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "algorithmsId", value = "算法id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "dynamicAnalysisInfoId", value = "动态调用信息id",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "type", value = "类型 0-类结点，1-方法结点",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "partitionId", value = "划分信息id",  dataType = "int")
    })
    @ApiOperation(value = "执行划分", notes = "返回状态200成功")
    @RequestMapping(value = "/partitionResult/do", method = RequestMethod.GET)
    public JSONResult doPartition(String appid,String algorithmsid,String dynamicanalysisinfoid,int type,String partitionId) throws Exception {
        //partitionResultService.partition(appId,algorithmsId,dynamicAnalysisInfoId,type,partitionId);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/partition-detail/{id}", method = RequestMethod.GET)
    public JSONResult partitionResultDetail(@PathVariable String id) throws Exception {
        GraphDto graphDto = partitionResultService.queryPartitionResultForDto(id);
        return JSONResult.ok(graphDto);
    }

    @RequestMapping(value = "/partition-detail-node/{id}", method = RequestMethod.GET)
    public JSONResult partitionResultDetailNodes(@PathVariable String id, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        List<Object> nodes = partitionDetailService.queryPartitionDetailByResultIdPaged(id, page, pageSize);
        int count = partitionDetailService.countOfPartitionDetailByResultId(id);
        HashMap<String ,Object> result = new HashMap<>();
        result.put("list",wrapNodes(nodes));
        result.put("total",count);
        return JSONResult.ok(result);
    }
    private List<Object> wrapNodes(List<Object> nodes){
        List<Object> list = new ArrayList<>();
        for (Object node:nodes) {
            Object o = null;
            if(node instanceof ClassNode){
                ClassNode classNode = (ClassNode) node;
                o = new ClassDto();
                ((ClassDto) o).setId(classNode.getId());
                ((ClassDto) o).setName(classNode.getName());
            }else if (node instanceof MethodNode){
                log.error("node class type Method");
            }else{
                log.error("node class type wrong");
            }
            list.add(o);
        }
        return list;
    }
    @RequestMapping(value = "/partition-results/{partitionInfoId}", method = RequestMethod.PUT)
    public JSONResult partitionMoveResultDetailNodes(@PathVariable String partitionInfoId, String nodeId, String  oldPartitionResultName, String targetPartitionResultName) {

        PartitionGraphOperateDto dto = partitionDetailService.moveNodeToPartition(nodeId,
                partitionResultService.queryPartitionResult(partitionInfoId, oldPartitionResultName),
                partitionResultService.queryPartitionResult(partitionInfoId, targetPartitionResultName));

        return JSONResult.ok(dto);
    }

    @RequestMapping(value = "/partition-detail-edge/{id}", method = RequestMethod.GET)
    public JSONResult partitionResultDetailEdges(@PathVariable String id, Integer page, Integer pageSize) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        List<PartitionResultEdgeCall> list = partitionResultEdgeService.findPartitionResultEdgeCallByEdgeId(id, page, pageSize);
        int count = partitionResultEdgeService.countOfPartitionResultEdgeCallByEdgeId(id);
        HashMap<String ,Object> result = new HashMap<>();
        result.put("list",wrapEdges(list));
        result.put("total",count);
        return JSONResult.ok(result);
    }

    private List<CallDto> wrapEdges(List<PartitionResultEdgeCall> edges){
        List<CallDto> list = new ArrayList<>();
        for (PartitionResultEdgeCall edge:edges) {
            CallDto callDto = new CallDto();
            callDto.setId(edge.getId());
            Object call = edge.getCall();
            if (call instanceof StaticCallInfo){
                StaticCallInfo staticCallInfo = (StaticCallInfo) call;
                Object caller = staticCallInfo.getCallerObj();
                callDto.setCaller(wrapCallObj(caller));
                Object callee = staticCallInfo.getCalleeObj();
                callDto.setCallee(wrapCallObj(callee));
            }else if(call instanceof DynamicCallInfo){
                DynamicCallInfo dynamicCallInfo = (DynamicCallInfo) call;
                Object caller = dynamicCallInfo.getCallerObj();
                callDto.setCaller(wrapCallObj(caller));
                Object callee = dynamicCallInfo.getCalleeObj();
                callDto.setCallee(wrapCallObj(callee));
            }else{

            }
            list.add(callDto);
        }
        return list;
    }

    private Object wrapCallObj(Object callObj){
        if (callObj instanceof ClassNode){
            ClassNode callerClass = (ClassNode) callObj;
            ClassDto classDto = new ClassDto();
            classDto.setId(callerClass.getId());
            classDto.setName(callerClass.getName());
            return classDto;
        }else if(callObj instanceof  MethodNode){
            log.error("caller class type method");
        }else{
            log.error("caller class type wrong");
        }
        return null;
    }
}
