package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.RePartitionDto;
import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.PartitionNodeEdgeService;
import cn.edu.nju.software.sda.app.service.PartitionNodeService;
import cn.edu.nju.software.sda.app.service.PartitionService;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "划分相关接口")
@RequestMapping(value = "/api")
public class PartitionController {
    @Autowired
    private PartitionService partitionService;
    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @ApiModelProperty(value = "partition", notes = "项目信息的json串")
    @ApiOperation(value = "新增划分", notes = "返回状态200成功")
    @RequestMapping(value = "/partition", method = RequestMethod.POST)
    public JSONResult addPartition(@RequestBody PartitionInfoEntity partition) throws Exception {
        partitionService.savePartition(partition);
        return JSONResult.ok();
    }

    @ApiModelProperty(value = "partition", notes = "项目信息的json串")
    @ApiOperation(value = "更新项目", notes = "返回状态200成功")
    @RequestMapping(value = "/partition", method = RequestMethod.PUT)
    public ResponseEntity updatePartition(@RequestBody PartitionInfoEntity partition) throws Exception {
        return ResponseEntity.ok(partitionService.updatePartition(partition));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除项目", notes = "返回状态200成功")
    @RequestMapping(value = "/partition/{id}", method = RequestMethod.DELETE)
    public JSONResult deletePartition(@PathVariable String id) throws Exception {
        partitionService.delPartition(id);
        return JSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "分页查询项目列表", notes = "返回状态200成功")
    @RequestMapping(value = "/partition", method = RequestMethod.GET)
    public ResponseEntity queryPartitionListPaged(Integer page, Integer pageSize, String appId, String desc) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PartitionInfoEntity partitionInfoEntity = new PartitionInfoEntity();
        partitionInfoEntity.setAppId(appId);
        if(StringUtils.isNotBlank(desc))
            partitionInfoEntity.setDesc(desc);
        PageQueryDto<PartitionInfoEntity> dto = partitionService.queryPartitionInfoPaged( PageQueryDto.create(page, pageSize), partitionInfoEntity);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/partition/re_relation", method = RequestMethod.POST)
    public ResponseEntity queryPartitionListPaged(@RequestBody RePartitionDto dto) {
        partitionNodeEdgeService.resetPartitionPair(dto.getPartitionInfoId(), dto.getRelationInfoIds());
        partitionNodeEdgeService.statisticsPartitionResultEdge(dto.getPartitionInfoId());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/partition/copy/{partitionInfoId}")
    public ResponseEntity copyPartition(@PathVariable String partitionInfoId) {
        PartitionInfoEntity partitionInfoEntity = partitionService.copyByInfoId(partitionInfoId);
        return ResponseEntity.ok(partitionInfoEntity);
    }



//    @ApiOperation(data = "获取划分详情", notes = "返回状态200成功")
//    @RequestMapping(data = "/partition/{id}", method = RequestMethod.GET)
//    public JSONResult getGraph(@PathVariable String id) {
//        PartitionGraph artitionGraph = partitionService.getGraph(id);
//        return JSONResult.ok(artitionGraph);
//    }
}
