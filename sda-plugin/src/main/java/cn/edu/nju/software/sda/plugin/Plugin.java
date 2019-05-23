package cn.edu.nju.software.sda.plugin;

public interface Plugin {

    String getName();

    String getDesc();

    void install();

    void uninstall();
}
