package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.PartitionService;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "划分相关接口")
@RequestMapping(value = "/api")
public class PartitionController {
    @Autowired
    private PartitionService partitionService;

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
    public ResponseEntity queryPartitionListPaged(Integer page, Integer pageSize, String appId) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PartitionInfoEntity partitionInfoEntity = new PartitionInfoEntity();
        partitionInfoEntity.setAppId(appId);
        PageQueryDto<PartitionInfoEntity> dto = partitionService.queryPartitionInfoPaged( PageQueryDto.create(page, pageSize), partitionInfoEntity);
        return ResponseEntity.ok(dto);
    }

//    @ApiOperation(data = "获取划分详情", notes = "返回状态200成功")
//    @RequestMapping(data = "/partition/{id}", method = RequestMethod.GET)
//    public JSONResult getGraph(@PathVariable String id) {
//        PartitionGraph artitionGraph = partitionService.getGraph(id);
//        return JSONResult.ok(artitionGraph);
//    }
}
