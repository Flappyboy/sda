package cn.edu.nju.software.sda.plugin.function.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileCompress;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import org.springframework.asm.ClassReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StaticJavaInfoCollection extends InfoCollection {

    String dataName = "Jar/War";

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem(dataName, FormDataType.FILE));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {

        File file = (File) inputData.getFormDataObjs(getMetaData()).get(dataName).get(0);

        ArrayList<String> myfiles = new ArrayList<String>();
        String path;
        String outPath = work.getWorkspace().getAbsolutePath();
        System.out.println("解压路径：" + outPath);
        FileCompress.unCompress(file.getAbsolutePath(), outPath);
        if (file.getName().trim().endsWith(".war"))
            path = outPath + "/WEB-INF/classes";
        else
            path = outPath;

        traverseFolder(path, myfiles);
        System.out.println("class文件数：" + myfiles.size());
        JavaData data = new JavaData();
        try {
            for (String classfile : myfiles) {
                if (classfile.endsWith(".class")) {
                    InputStream inputstream = new FileInputStream(new File(classfile));
                    ClassReader cr = new ClassReader(inputstream);
                    ClassAdapter ca = new ClassAdapter(data);
                    cr.accept(ca, ClassReader.EXPAND_FRAMES);
                }
            }
            return new InfoSet(data.getInfos());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void traverseFolder(String path, ArrayList<String> myfiles) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder(file2.getAbsolutePath(), myfiles);
                    } else {

                        if (file2.getName().endsWith(".class")) {
                            myfiles.add(file2.getAbsolutePath());
//                            System.out.println("文件:" + file2.getAbsolutePath());
                        }

                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    @Override
    public String getName() {
        return "sys_StaticJavaInfoCollection";
    }

    @Override
    public String getDesc() {
        return "collect java program static data.";
    }
}
