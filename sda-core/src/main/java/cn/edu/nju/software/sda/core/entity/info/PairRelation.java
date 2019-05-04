package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;

public class PairInfo<T extends Node> extends Info<T> {

    public static enum Direction {
        FORWORD("Forword"),
        ;

        private String name;

        Direction(String name) {
            this.name = name;
        }
    }

    private Node sourceNode;

    private Node targetNode;
}
