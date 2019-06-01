package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.util;

import java.io.File;
import java.util.*;

public class GetAllFiles {
    private  List<String> nowFiles;

    public static void main(String[] args){
        String catalog = "/Users/yaya/Desktop/dddsample-core";
        List<String> t = new GetAllFiles().getNowAllFiles(catalog);
        for (int i=0; i<t.size(); i++){
            System.out.println(t.get(i));
        }
    }

    public  List<String> getNowAllFiles(String catalog){
        nowFiles = new ArrayList<>();
        String fileName= catalog + File.separator;
        File f=new File(fileName);
        print(f, catalog);
        return nowFiles;
    }

    public  void print(File f, String catalog){
        if(f!=null){
            if(f.isDirectory()){
                File[] fileArray=f.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                        print(fileArray[i], catalog);
                    }
                }
            }
            else{
                String name = f.getPath();
                String name2 = name.replace("\\", "/");
                String catalog2 = catalog.replace("\\", "/");
                String[] temp = name2.split("/");
                String[] cat = catalog2.split("/");

                String path = temp[cat.length];
                for (int i=cat.length+1; i<temp.length; i++){
                    path += "/"+temp[i];
                }
                nowFiles.add(path);
            }
        }
    }

}

