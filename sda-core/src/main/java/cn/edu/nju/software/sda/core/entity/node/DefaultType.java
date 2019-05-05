package cn.edu.nju.software.sda.core.entity.node;

public class DefaultType implements Type {
    private static DefaultType obj = new DefaultType();

    public static DefaultType getInstance(){
        return obj;
    }

    private DefaultType(){};
    @Override
    public String getName() {
        return "Default";
    }
}
