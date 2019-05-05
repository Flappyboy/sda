package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import lombok.Data;

import java.util.*;

@Data
public class RelationInfo<R extends Relation> extends Info implements Iterable<R> {

    Class nodeClass;
    Class relationClass;

    public RelationInfo(String name, Class nodeClass, Class relationClass) {
        this.setName(name);
        this.nodeClass = nodeClass;
        this.relationClass = relationClass;
    }

    private HashMap<R, R> relations = new HashMap<>();

    public RelationInfo addRelation(R relation) throws UnexpectedClassException {
        check(relation);

        relations.put(relation, relation);
        return this;
    }
    public RelationInfo addRelationByAddValue(R relation) throws UnexpectedClassException {
        check(relation);

        if(relations.containsKey(relation)){
            relations.get(relation).addValue(relation.getValue());
        }
        return this;
    }

    private void check(R relation) {
        if(!relationClass.isAssignableFrom(relation.getClass()) ){
            throw new UnexpectedClassException(relationClass, relation.getClass());
        }
        if(!nodeClass.isAssignableFrom(relation.getNodeClass())){
            throw new UnexpectedClassException(nodeClass, relation.getNodeClass());
        }
    }

    @Override
    public Iterator<R> iterator() {
        return relations.values().iterator();
    }

    public int size() {
        return relations.size();
    }
}
