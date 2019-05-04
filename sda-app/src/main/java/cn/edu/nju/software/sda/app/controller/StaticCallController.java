package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.StaticCallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "静态调用关系接口")
@RequestMapping(value = "/api")
public class StaticCallController {
    @Autowired
    private StaticCallService staticCallService;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "path", value = "划分项目路径", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "appId", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "静态代码分析，结果入库", notes = "返回状态200成功")
    @RequestMapping(value = "/staticCall/do", method = RequestMethod.GET)
    public JSONResult doStaticAnalysis(String path, String appid,Integer flag) throws Exception {
        try {
            System.out.println(path);
            //staticCallService.saveStaticAnalysis(appId, path,flag);
            return JSONResult.ok();
        } catch (Exception e) {
            return JSONResult.errorMsg(e.toString());
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "appId", value = "项目appid", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "type", value = "结点类型，0-类结点，1-方法结点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "静态结果分页列表", notes = "返回状态200成功")
    @RequestMapping(value = "/staticCall", method = RequestMethod.GET)
    public JSONResult staticEdgeList(String appid, Integer page, Integer pageSize, int type) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }

        List<HashMap<String, String>> data = staticCallService.findEdgeByAppId(appid, page, pageSize, type);
        int count = staticCallService.countOfStaticAnalysis(appid,type);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",data);
        result.put("total",count);
        return JSONResult.ok(result);
    }


}
