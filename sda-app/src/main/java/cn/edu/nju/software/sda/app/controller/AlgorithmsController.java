package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.Algorithms;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParam;
import cn.edu.nju.software.sda.app.entity.bean.AlgorithmsBean;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "算法相关接口")
@RequestMapping(value = "/api")
public class AlgorithmsController {
    @Autowired
    private AlgorithmsService algorithmsService;

    @ApiModelProperty(value = "algorithmsMap", notes = "算法的json串")
    @ApiOperation(value = "新增算法", notes = "返回状态200成功")
    @RequestMapping(value = "/addAlgorithms", method = RequestMethod.POST)
    public JSONResult addApp(@RequestBody AlgorithmsBean algorithmsMap) throws Exception {
        Algorithms algorithms = algorithmsMap.getAlgorithms();
        List<AlgorithmsParam> algorithmsParams = algorithmsMap.getParams();
        algorithmsService.saveAlgorithms(algorithms, algorithmsParams);
        return JSONResult.ok();
    }

    @ApiOperation(value = "根据id查询算法", notes = "返回状态200成功")
    @RequestMapping(value = "/algorithms", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "算法id", required = true, dataType = "String"),
    })
    public JSONResult queryAppById(String id) throws Exception {
        HashMap<String, Object> algorithms = algorithmsService.queryAlgorithmsById(id);
        return JSONResult.ok(algorithms);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页：页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "分页：每页大小（默认大小100）", dataType = "int")
    })
    @ApiOperation(value = "分页查询算法列表", notes = "返回状态200成功")
    @RequestMapping(value = "/algorithms/list", method = RequestMethod.GET)
    public JSONResult queryAppListPaged(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        List<Algorithms> algorithmsList = algorithmsService.queryAlgorithmsListPaged(page, pageSize);
        int count = algorithmsService.countOfAlgorithms();
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",algorithmsList);
        result.put("total",count);
        return JSONResult.ok(algorithmsList);
    }

    @ApiModelProperty(value = "algorithmsMap", notes = "算法的json串")
    @ApiOperation(value = "更新算法", notes = "返回状态200成功")
    @RequestMapping(value = "/updAlgorithms", method = RequestMethod.POST)
    public JSONResult updateAlgorithms(@RequestBody  AlgorithmsBean algorithmsMap) {
        Algorithms algorithms = algorithmsMap.getAlgorithms();
        List<AlgorithmsParam> algorithmsParams = algorithmsMap.getParams();
        algorithmsService.updateAlgorithms(algorithms, algorithmsParams);
        return JSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "算法id", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除算法", notes = "返回状态200成功")
    @RequestMapping(value = "/delAlgorithms", method = RequestMethod.GET)
    public JSONResult deleteAlgorithms(String id) {
        algorithmsService.deleteAlgorithms(id);
        return JSONResult.ok();
    }

}
