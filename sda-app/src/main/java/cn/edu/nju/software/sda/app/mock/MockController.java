package cn.edu.nju.software.sda.app.mock;

import cn.edu.nju.software.sda.app.mock.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value="/mock")
public class MockController {

    @PostMapping(value = "/api/app")
    @ResponseBody
    public ResponseDto addApp(@ModelAttribute AppDto dto) {
        dto.setCreateTime(System.currentTimeMillis());
        dto.setStatus(false);
        dto.setId(1003066940l);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dto);
        return responseDto;
    }

    @GetMapping(value = "/api/app")
    @ResponseBody
    public ResponseDto queryAppList(AppDto dto, @RequestParam int pageSize, @RequestParam int page) {
        List<AppDto> appDtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            AppDto appDto = getAppDto(1003066940l+pageSize*page+i);
            appDtoList.add(appDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(appDtoList);
        return responseDto;
    }

    @GetMapping(value = "/api/app/{id}")
    @ResponseBody
    public ResponseDto queryApp(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(getAppDto(id));
        return responseDto;
    }

    @DeleteMapping(value = "/api/app/{id}")
    @ResponseBody
    public ResponseDto delApp(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }

    private AppDto getAppDto(long id){
        AppDto appDto = new AppDto();
        appDto.setCreateTime(1546683527000l);
        appDto.setAppName("测试");
        appDto.setDesc("测试");
        appDto.setId(id);
        appDto.setClassCount(100);
        appDto.setFunctionCount(1000);
        appDto.setInterfaceCount(20);
        appDto.setInterFaceFunctionCount(150);
        appDto.setStatus(true);
        return appDto;
    }


    @PostMapping(value = "/api/statistics")
    @ResponseBody
    public ResponseDto addStatistics(@ModelAttribute StatisticsDto dto) {
        dto.setCreateTime(System.currentTimeMillis());
        dto.setStatus(false);
        dto.setId(1003066940l);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dto);
        return responseDto;
    }

    @GetMapping(value = "/api/statistics")
    @ResponseBody
    public ResponseDto queryStatisticsList(StatisticsDto dto, @RequestParam int pageSize, @RequestParam int page) {
        List<StatisticsDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            StatisticsDto statisticsDto = getStatisticsDto(1003066940l+pageSize*page+i);
            dtoList.add(statisticsDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }

    @GetMapping(value = "/api/statistics/{id}")
    @ResponseBody
    public ResponseDto queryStatistics(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(getStatisticsDto(id));
        return responseDto;
    }

    @DeleteMapping(value = "/api/statistics/{id}")
    @ResponseBody
    public ResponseDto delStatistics(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }

    private StatisticsDto getStatisticsDto(long id){
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setId(id);
        statisticsDto.setAppName("hap");
        statisticsDto.setCreateTime(1546683527000l);
        statisticsDto.setStartTime(1546683527000l);
        statisticsDto.setEndTime(1546683527000l);
        statisticsDto.setDesc("测试");
        statisticsDto.setStatus(true);
        return statisticsDto;
    }

    @GetMapping(value = "/api/call")
    @ResponseBody
    public ResponseDto queryCallList(CallDto dto, @RequestParam int pageSize, @RequestParam int page) {
        List<CallDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            CallDto callDto = getCallDto(1003066940l+pageSize*page+i);
            dtoList.add(callDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }
    private CallDto getCallDto(long id){
        CallDto callDto = new CallDto();
//        callDto.setId(id);
//        callDto.setCalleeName("TestCallee");
//        callDto.setCallerName("TestCaller");
        callDto.setCount(new Random().nextInt(1000));
        return callDto;
    }

    @PostMapping(value = "/api/partition")
    @ResponseBody
    public ResponseDto addPartition(@ModelAttribute PartitionDto dto) {
        dto.setCreateTime(System.currentTimeMillis());
        dto.setStatus(false);
        dto.setId(1003066940l);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dto);
        return responseDto;
    }

    @GetMapping(value = "/api/partition")
    @ResponseBody
    public ResponseDto queryPartitionList(PartitionDto dto, @RequestParam int pageSize, @RequestParam int page) {
        List<PartitionDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            PartitionDto partitionDto = getPartitionDto(1003066940l+pageSize*page+i);
            dtoList.add(partitionDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }

    @GetMapping(value = "/api/partition/{id}")
    @ResponseBody
    public ResponseDto queryPartition(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(getPartitionDto(id));
        return responseDto;
    }

    @DeleteMapping(value = "/api/partition/{id}")
    @ResponseBody
    public ResponseDto delPartition(@PathVariable long id) {
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }
    private PartitionDto getPartitionDto(long id){
        PartitionDto partitionDto = new PartitionDto();
        partitionDto.setId(id);
        partitionDto.setAppName("hap");
        partitionDto.setCreateTime(1546683527000l);
        partitionDto.setAlgorithmName("社区发现1");
        partitionDto.setStatisticsId(id);
        partitionDto.setDesc("测试");
        partitionDto.setTypeName("动态");
        partitionDto.setStatus(true);
        return partitionDto;
    }

    @GetMapping(value = "/api/class")
    @ResponseBody
    public ResponseDto queryCallList(ClassDto dto, @RequestParam int pageSize, @RequestParam int page) {
        List<CallDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            CallDto callDto = getCallDto(1003066940l+pageSize*page+i);
            dtoList.add(callDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }
    private ClassDto getClassDto(long id){
        ClassDto classDto = new ClassDto();
//        classDto.setId(id);
//        classDto.setPackageName("com.edu.nju.test");
//        classDto.setSimpleName("Test");
        return classDto;
    }

    @GetMapping(value = "/api/partition-detail")
    @ResponseBody
    public ResponseDto queryPartitonDetail() {
        GraphDto graphDto = new GraphDto();
        char c = 'A';
        for(int i=0; i< 10; i++){
            NodeDto nodeDto = new NodeDto();
            //nodeDto.setId(i+1l);
            nodeDto.setName(String.valueOf(c+i));
            nodeDto.setSize(new Random().nextInt(40)+10);
            graphDto.getNodes().add(nodeDto);
        }
//        EdgeDto edgeDto1 = new EdgeDto(1l,2l);
//        EdgeDto edgeDto2 = new EdgeDto(3l,4l);
//        EdgeDto edgeDto3 = new EdgeDto(5l,5l);
//        EdgeDto edgeDto4 = new EdgeDto(2l,7l);
//        EdgeDto edgeDto5 = new EdgeDto(7l,4l);
//        EdgeDto edgeDto6 = new EdgeDto(4l,1l);
//        EdgeDto edgeDto7 = new EdgeDto(3l,8l);
//        EdgeDto edgeDto8 = new EdgeDto(7l,2l);
//        EdgeDto edgeDto9 = new EdgeDto(4l,2l);
//        EdgeDto edgeDto10 = new EdgeDto(3l,10l);
//        graphDto.getLinks().add(edgeDto1);
//        graphDto.getLinks().add(edgeDto2);
//        graphDto.getLinks().add(edgeDto3);
//        graphDto.getLinks().add(edgeDto4);
//        graphDto.getLinks().add(edgeDto5);
//        graphDto.getLinks().add(edgeDto6);
//        graphDto.getLinks().add(edgeDto7);
//        graphDto.getLinks().add(edgeDto8);
//        graphDto.getLinks().add(edgeDto9);
//        graphDto.getLinks().add(edgeDto10);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(graphDto);
        return responseDto;
    }

    @GetMapping(value = "/api/edge")
    @ResponseBody
    public ResponseDto queryEdgeList(@RequestParam int pageSize, @RequestParam int page) {
        List<CallDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            CallDto callDto = getCallDto(1003066940l+pageSize*page+i);
            dtoList.add(callDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }
    @GetMapping(value = "/api/node")
    @ResponseBody
    public ResponseDto queryNodeList(@RequestParam int pageSize, @RequestParam int page) {
        List<ClassDto> dtoList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            ClassDto classDto = getClassDto(1003066940l+pageSize*page+i);
            dtoList.add(classDto);
        }
        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(dtoList);
        return responseDto;
    }
}
