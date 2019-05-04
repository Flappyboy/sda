package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.app.utils.FileUtil;
import cn.edu.nju.software.sda.app.utils.asm.ClassAdapter;
import cn.edu.nju.software.sda.app.utils.asm.MethodAdapter;
import cn.edu.nju.software.sda.app.utils.file.FileCompress;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.asm.ClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class StaticCallServiceImpl implements StaticCallService {

    @Autowired
    private ClassNodeMapper classNodeMapper;
    @Autowired
    private ClassNodeMapperExtend classNodeMapperExtend;
    @Autowired
    private MethodNodeMapper methodNodeMapper;
    @Autowired
    private StaticCallInfoMapper staticCallInfoMapper;
    @Autowired
    private ClassNodeService classNodeService;
    @Autowired
    private MethodNodeService methodNodeService;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private PinpointPluginService pinpointPluginService;
    @Autowired
    private Sid sid;

    @Override
    public StaticCallInfo queryCallById(String id) {
        StaticCallInfo staticCallInfo = staticCallInfoMapper.selectByPrimaryKey(id);
        if (staticCallInfo!=null){
            staticCallInfo.setCallerObj(queryCallObj(staticCallInfo.getCaller(),staticCallInfo.getType()));
            staticCallInfo.setCalleeObj(queryCallObj(staticCallInfo.getCallee(),staticCallInfo.getType()));
        }
        return staticCallInfo;
    }

    private Object queryCallObj(String id, int type){
        if(type==0){
            return classNodeMapper.selectByPrimaryKey(id);
        }else{
            return methodNodeMapper.selectByPrimaryKey(id);
        }
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveStaticAnalysis(App app,Integer flag) throws Exception {
        String compressFile = app.getPath();
        ArrayList<String> myfiles = new ArrayList<String>();
        String path = "";

        if(flag != null) {
            if(flag == 1) {
                String outPath = compressFile.trim().substring(0, compressFile.trim().lastIndexOf("."));
                System.out.println("解压路径：" + outPath);
                FileCompress.unCompress(compressFile, outPath);
                if (path.trim().endsWith(".war"))
                    path = outPath + "/WEB-INF/classes";
                else
                    path = outPath;
            }else
                path = compressFile;
        }else
            path = compressFile;
        FileUtil.traverseFolder(path, myfiles);
        System.out.println("class文件数：" + myfiles.size());
        for (String file : myfiles) {
            if (file.endsWith(".class")) {
                InputStream inputstream = new FileInputStream(new File(file));
                ClassReader cr = new ClassReader(inputstream);
                ClassAdapter ca = new ClassAdapter();
                cr.accept(ca, ClassReader.EXPAND_FRAMES);
            }
        }
        System.out.println("++++++++++++++");
        System.out.println(ClassAdapter.getMethodNodes().size());
        System.out.println(MethodAdapter.getMethodEdges().size());
        System.out.println("++++++++++++++");

        //方法
        Map<String, MethodNode> methodnoedes = ClassAdapter.getMethodNodes();
        Map<String, StaticCallInfo> methodedges = MethodAdapter.getMethodEdges();
        //类
        Map<String, ClassNode> classNodes = ClassAdapter.getClassNodes();
        Map<String, StaticCallInfo> classEdges = MethodAdapter.getClassEdges();



        System.out.println("保存类结点");
        saveClassNode(classNodes, app.getId());
        System.out.println("保存类边");
        saveClassEdge(classEdges, app.getId());
        System.out.println("保存方法结点");
//        saveMethodNode(methodnoedes, appId);
        System.out.println("保存方法边");
//        saveMethodEdge(methodedges, appId);

        log.debug("开始生成pinpoint插件");

        pinpointPluginService.generatePlugin(app);

        App new_app = new App();
        new_app.setId(app.getId());
        new_app.setStatus(1);
        new_app.setClasscount(classNodes.size());
        new_app.setFunctioncount(methodnoedes.size());
        new_app.setInterfacecount(ClassAdapter.interfaceNum);
        appMapper.updateByPrimaryKeySelective(new_app);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束！");
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<HashMap<String, String>> findEdgeByAppId(String appid, int page, int pageSize, int type) {
        List<HashMap<String, String>> edges = new ArrayList<>();
        PageHelper.startPage(page, pageSize);

        StaticCallInfo sciDemo = new StaticCallInfo();
        sciDemo.setAppId(appid);
        sciDemo.setFlag(1);
        sciDemo.setType(type);
        Example example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(sciDemo);
        example.setOrderByClause("count");
        List<StaticCallInfo> scinfos = staticCallInfoMapper.selectByExample(example);

        for (StaticCallInfo scinfo : scinfos) {
            HashMap<String, String> edge = new HashMap<String, String>();
            if (type == 0) {
                ClassNode callerNode = classNodeService.findById(scinfo.getCaller());
                ClassNode calleeNode = classNodeService.findById(scinfo.getCallee());
                edge.put("caller", callerNode.getName());
                edge.put("callee", calleeNode.getName());
                edge.put("count", String.valueOf(scinfo.getCount()));
            }
            if (type == 1) {
                MethodNode callerNode = methodNodeService.findById(scinfo.getCaller());
                MethodNode calleeNode = methodNodeService.findById(scinfo.getCallee());
                edge.put("caller", callerNode.getName());
                edge.put("callee", calleeNode.getName());
                edge.put("count", String.valueOf(scinfo.getCount()));
            }
            edges.add(edge);
        }
        return edges;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfStaticAnalysis(String appId, int type) {
        StaticCallInfo sciDemo = new StaticCallInfo();
        sciDemo.setAppId(appId);
        sciDemo.setFlag(1);
        sciDemo.setType(type);
        Example example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(sciDemo);

        return staticCallInfoMapper.selectCountByExample(example);
    }

    @Override
    public List<StaticCallInfo> findByAppIdAndType(String appId, int type) {
        StaticCallInfo sciDemo = new StaticCallInfo();
        sciDemo.setAppId(appId);
        sciDemo.setFlag(1);
        sciDemo.setType(type);
        Example example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(sciDemo);

        return staticCallInfoMapper.selectByExample(example);
    }

    private void saveMethodNode(HashMap<String, MethodNode> methodnoedes, String appid) {
        int methodKey = 0;
        for (Map.Entry<String, MethodNode> entry : methodnoedes.entrySet()) {
            MethodNode methodNode = entry.getValue();
//            System.out.println(methodNode);
            MethodNodeExample example = new MethodNodeExample();
            MethodNodeExample.Criteria criteria = example.createCriteria();
            criteria.andFullNameEqualTo(methodNode.getFullname())
//                    .andNameEqualTo(methodNode.getName())
//                    .andClassnameEqualTo(methodNode.getClassname())
                    .andAppidEqualTo(appid).andFlagEqualTo(1);
//            criteria.andNameEqualTo(methodNode.getName()).andClassnameEqualTo(methodNode.getClassname())
//                    .andAppidEqualTo(appId).andFlagEqualTo(1);
//            List<MethodNode> mnodes = methodNodeMapper.selectByExample(example);
            List<MethodNode> mnodes = methodNodeMapper.selectByExampleWithBLOBs(example);

            methodKey += 1;

            if (mnodes.size() == 0 || mnodes == null) {
                String id = sid.nextShort();

                String classId = "";
                ClassNodeExample classexample = new ClassNodeExample();
                ClassNodeExample.Criteria classcriteria = classexample.createCriteria();
                classcriteria.andNameEqualTo(methodNode.getClassname()).andAppidEqualTo(appid).andFlagEqualTo(1);
                List<ClassNode> cnodes = classNodeMapper.selectByExample(classexample);
                if (cnodes.size() != 0 && cnodes != null) {
                    ClassNode cn = cnodes.get(0);
                    classId = cn.getId();
                }

                methodNode.setId(id);
                methodNode.setClassid(classId);
                methodNode.setAppid(appid);
                methodNode.setFlag(1);
                methodNode.setKey(methodKey);
                methodNode.setCreatedat(new Date());
                methodNode.setUpdatedat(new Date());
                methodNodeMapper.insert(methodNode);
            } else {
                MethodNode mn = mnodes.get(0);
                mn.setKey(methodKey);
                methodNodeMapper.updateByPrimaryKeySelective(mn);
//                MethodNodeExample example1 = new MethodNodeExample();
//                MethodNodeExample.Criteria criteria1 = example1.createCriteria();
//                criteria1.andIdEqualTo(mn.getId()).andFlagEqualTo(1);
//                methodNodeMapper.updateByExampleSelective(mn, example1);
            }
        }

    }
    private void saveMethodNodeBatch(HashMap<String, MethodNode> methodnoedes, String appid) {
        int methodKey = 0;
        for (Map.Entry<String, MethodNode> entry : methodnoedes.entrySet()) {
            MethodNode methodNode = entry.getValue();
            MethodNodeExample example = new MethodNodeExample();
            MethodNodeExample.Criteria criteria = example.createCriteria();
            criteria.andFullNameEqualTo(methodNode.getFullname())
                    .andAppidEqualTo(appid).andFlagEqualTo(1);
            List<MethodNode> mnodes = methodNodeMapper.selectByExampleWithBLOBs(example);

            methodKey += 1;

            if (mnodes.size() == 0 || mnodes == null) {
                String id = sid.nextShort();

                String classId = "";
                ClassNodeExample classexample = new ClassNodeExample();
                ClassNodeExample.Criteria classcriteria = classexample.createCriteria();
                classcriteria.andNameEqualTo(methodNode.getClassname()).andAppidEqualTo(appid).andFlagEqualTo(1);
                List<ClassNode> cnodes = classNodeMapper.selectByExample(classexample);
                if (cnodes.size() != 0 && cnodes != null) {
                    ClassNode cn = cnodes.get(0);
                    classId = cn.getId();
                }

                methodNode.setId(id);
                methodNode.setClassid(classId);
                methodNode.setAppid(appid);
                methodNode.setFlag(1);
                methodNode.setKey(methodKey);
                methodNode.setCreatedat(new Date());
                methodNode.setUpdatedat(new Date());
                methodNodeMapper.insert(methodNode);
            } else {
                MethodNode mn = mnodes.get(0);
                mn.setKey(methodKey);
                methodNodeMapper.updateByPrimaryKeySelective(mn);
            }
        }

    }

    private void saveMethodEdge(Map<String, StaticCallInfo> methodedges, String appid) {
        for (Map.Entry<String, StaticCallInfo> entry : methodedges.entrySet()) {
            String sourceid = "";
            String targetid = "";
            StaticCallInfo methodEdge = entry.getValue();

//            System.out.println("getSource   :    " + methodEdge.getCaller());
            String sCallerName = methodEdge.getCaller().replace("--!--",".").replace("-!-","");
//            String[] scallerArr = sCallerName.split("-!-");
            MethodNodeExample example1 = new MethodNodeExample();
            MethodNodeExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andFullNameEqualTo(sCallerName.replace("-!-","")).andAppidEqualTo(appid).andFlagEqualTo(1);
//            criteria1.andNameEqualTo(scallerArr[1]).andClassnameEqualTo(scallerArr[0]).andAppidEqualTo(appId).andFlagEqualTo(1);
            List<MethodNode> sourceNodes = methodNodeMapper.selectByExampleWithBLOBs(example1);
            if (!sourceNodes.isEmpty()) {
                MethodNode mynode = sourceNodes.get(0);
                sourceid = mynode.getId();
            }

//            System.out.println("getTarget   :   " + methodEdge.getCallee());
            String tCalleeName = methodEdge.getCallee();
//            String[] tcalleeArr = tCalleeName.split("-!-");
            MethodNodeExample example2 = new MethodNodeExample();
            MethodNodeExample.Criteria criteria2 = example2.createCriteria();
            criteria1.andFullNameEqualTo(tCalleeName.replace("-!-","")).andAppidEqualTo(appid).andFlagEqualTo(1);
//            criteria2.andNameEqualTo(tcalleeArr[1]).andClassnameEqualTo(tcalleeArr[0]).andAppidEqualTo(appId).andFlagEqualTo(1);
            List<MethodNode> targetNodes = methodNodeMapper.selectByExampleWithBLOBs(example2);
            if (!targetNodes.isEmpty()) {
                MethodNode mynode = targetNodes.get(0);
                targetid = mynode.getId();
            }

            if (sourceid != "" && targetid != "") {

                StaticCallInfo sciDemo = new StaticCallInfo();
                sciDemo.setCaller(sourceid);
                sciDemo.setCallee(targetid);
                sciDemo.setAppId(appid);
                sciDemo.setType(1);
                sciDemo.setFlag(1);
                Example example = new Example(StaticCallInfo.class);
                example.createCriteria().andEqualTo(sciDemo);
                List<StaticCallInfo> myedge = staticCallInfoMapper.selectByExample(example);
                if (myedge.size() == 0 || myedge == null) {
                    String id = sid.nextShort();
                    int count = methodEdge.getCount();
                    StaticCallInfo record = new StaticCallInfo();
                    record.setId(id);
                    record.setCaller(sourceid);
                    record.setCallee(targetid);
                    record.setCount(count);
                    record.setAppId(appid);
                    record.setCreatedAt(new Date());
                    record.setUpdatedAt(new Date());
                    record.setFlag(1);
                    record.setType(1);
                    staticCallInfoMapper.insert(record);
                }
            }

        }
    }

    private void saveClassNode(Map<String, ClassNode> classNodes, String appid) {
        int classKey = 0;

        for (Map.Entry<String, ClassNode> entry : classNodes.entrySet()) {
            ClassNode classNode = entry.getValue();

            ClassNodeExample example = new ClassNodeExample();
            ClassNodeExample.Criteria criteria = example.createCriteria();
            criteria.andNameEqualTo(classNode.getName()).andAppidEqualTo(appid).andFlagEqualTo(1);
            List<ClassNode> cnodes = classNodeMapper.selectByExample(example);

            classKey += 1;

            if (cnodes.size() == 0 || cnodes == null) {
                String id = sid.nextShort();
                classNode.setId(id);
                classNode.setAppid(appid);
                classNode.setFlag(1);
                classNode.setKey(classKey);
                classNode.setCreatedat(new Date());
                classNode.setUpdatedat(new Date());
                classNodeMapper.insert(classNode);
            } else {
                ClassNode cn = cnodes.get(0);
                cn.setKey(classKey);
                cn.setUpdatedat(new Date());
                ClassNodeExample example1 = new ClassNodeExample();
                ClassNodeExample.Criteria criteria1 = example1.createCriteria();
                criteria1.andIdEqualTo(cn.getId()).andFlagEqualTo(1);
                classNodeMapper.updateByExampleSelective(cn, example1);
            }
        }
    }
    private void saveClassNodeBatch(Map<String, ClassNode> classNodes, String appid) {
        int classKey = 0;

        for (Map.Entry<String, ClassNode> entry : classNodes.entrySet()) {
            ClassNode classNode = entry.getValue();

            classKey += 1;

            String id = sid.nextShort();
            classNode.setId(id);
            classNode.setAppid(appid);
            classNode.setFlag(1);
            classNode.setKey(classKey);
            classNode.setCreatedat(new Date());
            classNode.setUpdatedat(new Date());
        }
        List<ClassNode> classNodeList = new ArrayList<>(classNodes.values());
        classNodeMapperExtend.insertBatch(classNodeList);
    }

    private void saveClassEdge(Map<String, StaticCallInfo> classEdges, String appid) {
        for (Map.Entry<String, StaticCallInfo> entry : classEdges.entrySet()) {
            String sourceid = "";
            String targetid = "";
            StaticCallInfo classEdge = entry.getValue();

//            System.out.println("getSource   :    " + classEdge.getCaller());
            ClassNodeExample example1 = new ClassNodeExample();
            ClassNodeExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andNameEqualTo(classEdge.getCaller()).andAppidEqualTo(appid).andFlagEqualTo(1);
            List<ClassNode> sourceNodes = classNodeMapper.selectByExample(example1);
            if (!sourceNodes.isEmpty()) {
                ClassNode mynode = sourceNodes.get(0);
                sourceid = mynode.getId();
            }

//            System.out.println("getTarget   :   " + classEdge.getCallee());
            ClassNodeExample example2 = new ClassNodeExample();
            ClassNodeExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andNameEqualTo(classEdge.getCallee()).andAppidEqualTo(appid).andFlagEqualTo(1);
            List<ClassNode> targetNodes = classNodeMapper.selectByExample(example2);
            if (!targetNodes.isEmpty()) {
                ClassNode mynode = targetNodes.get(0);
                targetid = mynode.getId();
            }

            if (sourceid != "" && targetid != "") {
                StaticCallInfo sciDemo = new StaticCallInfo();
                sciDemo.setCaller(sourceid);
                sciDemo.setCallee(targetid);
                sciDemo.setAppId(appid);
                sciDemo.setType(1);
                sciDemo.setFlag(1);
                Example example = new Example(StaticCallInfo.class);
                example.createCriteria().andEqualTo(sciDemo);
                List<StaticCallInfo> myedge = staticCallInfoMapper.selectByExample(example);
                if (myedge.size() == 0 || myedge == null) {
                    String id = sid.nextShort();
                    int count = classEdge.getCount();
                    StaticCallInfo record = new StaticCallInfo();
                    record.setId(id);
                    record.setCaller(sourceid);
                    record.setCallee(targetid);
                    record.setCount(count);
                    record.setAppId(appid);
                    record.setCreatedAt(new Date());
                    record.setUpdatedAt(new Date());
                    record.setFlag(1);
                    record.setType(0);
                    staticCallInfoMapper.insert(record);
                }
            }

        }
    }
}
