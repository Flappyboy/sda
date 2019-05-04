package cn.edu.nju.software.sda.core.exception;

public class UnexpectedClassException extends RuntimeException{
    public UnexpectedClassException(Class expectedClass, Class receiveClass) {
        super("Expected "+expectedClass.getName()+" but receive "+receiveClass.getName());
    }
}
