package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@ToString
public abstract class RelationInfo<R extends Relation> extends Info implements Iterable<R> {

    public RelationInfo(String name) {
        super(name);
    }

    private HashMap<R, R> relations = new HashMap<>();//detail中的

    public RelationInfo addRelation(R relation) throws UnexpectedClassException {
        relations.put(relation, relation);
        return this;
    }
    public RelationInfo addRelationByAddValue(R relation) throws UnexpectedClassException {
        if(relations.containsKey(relation)){
            relations.get(relation).addValue(relation.getValue());
        }else{
            relations.put(relation, relation);
        }
        return this;
    }

    @Override
    public Iterator<R> iterator() {
        return relations.values().iterator();
    }

    public int size() {
        return relations.size();
    }
}
