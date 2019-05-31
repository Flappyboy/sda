package cn.edu.nju.software.sda.plugin.function.info.impl.pinpointplugin.command;

import java.util.Date;

public class CommandResult {

    private String command;
    private Date startTime;
    private Date endTime;
    private Integer exitValue;

    private StringBuffer output = new StringBuffer();
    private StringBuffer errorOutput = new StringBuffer();

    public CommandResult(String command, Date startTime) {
        this.command = command;
        this.startTime = startTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getExitValue() {
        return exitValue;
    }

    public void setExitValue(Integer exitValue) {
        this.exitValue = exitValue;
    }

    public void appendOutput(String sb){
        output.append(sb);
        output.append("\n");
    }

    public void appendErrorOutput(String sb){
        errorOutput.append(sb);
        output.append("\n");
    }

    public String getOutputForString(){
        return output.toString();
    }

    public String getErrorOutputForString(){
        return errorOutput.toString();
    }

}
