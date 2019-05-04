package cn.edu.nju.software.sda.app.utils.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class CommandUtils {

    public static CommandResult exec(String command){
        final CommandResult commandResult = new CommandResult(command, new Date());
        try {
            Process p = Runtime.getRuntime().exec(command);

            final InputStream is1 = p.getInputStream();
            new Thread(new Runnable() {
                public void run() {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                    try {
                        String outputLine = null;
                        while ((outputLine = br.readLine()) != null)
                            commandResult.appendOutput(outputLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            final InputStream is2 = p.getErrorStream();
            new Thread(new Runnable() {
                public void run() {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is2));
                    try {
                        String outputLine = null;
                        while ((outputLine = br.readLine()) != null)
                            commandResult.appendErrorOutput(outputLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            p.waitFor();
            commandResult.setEndTime(new Date());
            commandResult.setExitValue(p.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return commandResult;
    }


    public static void main(String[] args) throws IOException {

    }
}
