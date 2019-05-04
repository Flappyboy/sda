package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CallInfo {

    @Id
    private String id;

    private String caller;

    private String callee;

    private Integer count;

    private Integer flag;

    private Integer type;

    private Object callerObj;
    private Object calleeObj;

    public static List<CallInfo> creatCallInfoList(List<DynamicCallInfo> dynamicCallInfoList, List<StaticCallInfo> staticCallInfoList){
        List<CallInfo> callInfoList = new ArrayList<>();

        if(dynamicCallInfoList != null) {
            for (DynamicCallInfo obj :
                    dynamicCallInfoList) {
                callInfoList.add(createCallInfo(obj));
            }
        }
        if(staticCallInfoList != null) {
            for (StaticCallInfo obj :
                    staticCallInfoList) {
                callInfoList.add(createCallInfo(obj));
            }
        }

        return callInfoList;
    }

    public static CallInfo createCallInfo(DynamicCallInfo obj){
        if(obj == null){
            return null;
        }
        CallInfo callInfo = new CallInfo();
        callInfo.setId(obj.getId());
        callInfo.setCaller(obj.getCaller());
        callInfo.setCallee(obj.getCallee());
        callInfo.setCount(obj.getCount());
        callInfo.setFlag(obj.getFlag());
        callInfo.setType(obj.getType());
        callInfo.setCallerObj(obj.getCallerObj());
        callInfo.setCalleeObj(obj.getCalleeObj());
        return callInfo;
    }
    public static CallInfo createCallInfo(StaticCallInfo obj){
        if(obj == null){
            return null;
        }
        CallInfo callInfo = new CallInfo();
        callInfo.setId(obj.getId());
        callInfo.setCaller(obj.getCaller());
        callInfo.setCallee(obj.getCallee());
        callInfo.setCount(obj.getCount());
        callInfo.setFlag(obj.getFlag());
        callInfo.setType(obj.getType());
        callInfo.setCallerObj(obj.getCallerObj());
        callInfo.setCalleeObj(obj.getCalleeObj());
        return callInfo;
    }
}
