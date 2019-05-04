package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.DynamicCallInfo;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.DynamicCallService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "动态调用关系接口")
@RequestMapping(value = "/api")
public class DynamicCallController {

    @Autowired
    private DynamicCallService dynamicCallService;

    @ApiOperation(value = "动态结果分页列表", notes = "返回状态200成功")
    @RequestMapping(value = "/dynaCall", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "dynamicAnalysisInfoId", value = "动态信息id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "结点类型，0-类结点，1-方法结点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    public JSONResult dynamicEdgeList(String dynamicAnalysisInfoId, Integer page, Integer pageSize, int type) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }

        List<HashMap<String, String>> data = dynamicCallService.findEdgeByAppId(dynamicAnalysisInfoId, page, pageSize, type);
        int count = dynamicCallService.countOfDynamicCall(dynamicAnalysisInfoId,type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",data);
        result.put("total",count);
        return JSONResult.ok(result);
    }

    @ApiModelProperty(value = "dynamicAnalysisInfo", notes = "动态分析调用信息的json串")
    @ApiOperation(value = "新增动态分析调用信息", notes = "返回状态200成功")
    @RequestMapping(value = "/dynaCall", method = RequestMethod.POST)
    public JSONResult addApp(@RequestBody DynamicCallInfo dynamicCallInfo) throws Exception {
        dynamicCallService.saveDCallInfo(dynamicCallInfo);
        return JSONResult.ok();
    }
}
