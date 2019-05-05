package cn.edu.nju.software.sda.core.exception;

import cn.edu.nju.software.sda.core.entity.node.Type;

public class UnexpectedTypeException extends RuntimeException{
    public UnexpectedTypeException(Type receiveType) {
        super("Unexpected"+receiveType.getName());
    }
}
