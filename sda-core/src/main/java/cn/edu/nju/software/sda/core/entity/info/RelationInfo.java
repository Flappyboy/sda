package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class RelationInfo extends Info implements Iterable {

    Class nodeClass;
    Class relationClass;

    public RelationInfo(Class nodeClass, Class relationClass) {
        this.nodeClass = nodeClass;
        this.relationClass = relationClass;
    }

    List<Relation> relationList = new ArrayList<>();

    public RelationInfo addRelation(Relation relation) throws UnexpectedClassException {
        if(!relationClass.equals(relation.getClass()) ){
            throw new UnexpectedClassException(relationClass, relation.getClass());
        }
        if(!nodeClass.equals(relation.getNodeClass())){
            throw new UnexpectedClassException(nodeClass, relation.getNodeClass());
        }

        relationList.add(relation);
        return this;
    }

    @Override
    public Iterator<Relation> iterator() {
        return relationList.iterator();
    }
}
