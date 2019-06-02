package cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util;

import java.util.Comparator;

/**
 * @Auther: yaya
 * @Date: 2019/6/2 14:17
 * @Description:
 */
public class ComponentComparator implements Comparator<Component> {

    @Override
    public int compare(Component o1, Component o2) {
        return new Integer(o1.getSize()).compareTo(o2.getSize());
    }

}