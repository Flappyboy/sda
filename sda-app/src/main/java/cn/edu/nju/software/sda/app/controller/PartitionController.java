package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.PartitionInfo;
import cn.edu.nju.software.sda.app.entity.bean.PartitionGraph;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.PartitionService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
    public JSONResult addPartition(@RequestBody PartitionInfo partition) throws Exception {
        partitionService.addPartition(partition);
        return JSONResult.ok();
    }

    @ApiModelProperty(value = "partition", notes = "项目信息的json串")
    @ApiOperation(value = "更新项目", notes = "返回状态200成功")
    @RequestMapping(value = "/partition", method = RequestMethod.PUT)
    public JSONResult updatePartition(@RequestBody PartitionInfo partition) throws Exception {
        partitionService.updatePartition(partition);
        return JSONResult.ok();
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
    public JSONResult queryPartitionListPaged(Integer page, Integer pageSize,String appName,String desc,String algorithmsid,Integer type) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        List<HashMap<String ,Object>> partitionList = partitionService.findBycondition( page,  pageSize, appName, desc, algorithmsid, type);
        int count = partitionService.count( appName, desc, algorithmsid, type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",partitionList);
        result.put("total",count);
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "获取划分详情", notes = "返回状态200成功")
    @RequestMapping(value = "/partition/{id}", method = RequestMethod.GET)
    public JSONResult getGraph(@PathVariable String id) {
        PartitionGraph artitionGraph = partitionService.getGraph(id);
        return JSONResult.ok(artitionGraph);
    }


}
