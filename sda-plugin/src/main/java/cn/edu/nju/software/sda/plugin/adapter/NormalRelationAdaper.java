package cn.edu.nju.software.sda.plugin.adapter;

import cn.edu.nju.software.sda.core.domain.info.Relation;
import cn.edu.nju.software.sda.core.domain.info.RelationInfo;

public class NormalRelationAdaper<R extends Relation> extends RelationInfo<R> {
    private Double rangeMax = 1.0;

    private Double rangeMin = 0.0;

    public NormalRelationAdaper(RelationInfo<R> relationInfo) {
        super(relationInfo.getName());

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (R relation :
                relationInfo) {
            max = Double.max(max, relation.getValue());
            min = Double.min(min, relation.getValue());
        }
        if(min == max){
            max++;
        }

        double rate = (rangeMax - rangeMin) / (max - min);

        for (R relation :
                relationInfo) {
            Double value = rangeMin + rate * (relation.getValue()-min);
            R newRelation = (R) relation.clone();
            newRelation.setValue(value);
            addRelation(newRelation);
        }

    }
}
