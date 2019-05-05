package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@Api(value = "算法相关接口")
@RequestMapping(value = "/api")
public class AlgorithmsController {
    @Autowired
    private AlgorithmsService algorithmsService;

}
