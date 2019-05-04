package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;

public class PairRelation<T extends Node> extends Relation<T> {

    public static enum Direction {
        FORWORD("Forword"),
        REVERSE("reverse"),
        NONE("None")
        ;

        private String name;

        Direction(String name) {
            this.name = name;
        }
    }

    private Node sourceNode;

    private Node targetNode;
}
