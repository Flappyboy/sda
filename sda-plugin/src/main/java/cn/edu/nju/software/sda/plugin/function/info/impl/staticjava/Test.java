package cn.edu.nju.software.sda.plugin.function.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;

/**
 * @Auther: yaya
 * @Date: 2019/5/31 14:29
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        JavaData javaData = new JavaData();
        System.out.println(javaData.classNameFormat("se/citerus/dddsample/domain/model/handling/HandlingEvent"));
        System.out.println(javaData.methodNameFormat("se/citerus/dddsample/application/BookingService.bookNewCargo(Lse/citerus/dddsample/domain/model/location/UnLocode;Lse/citerus/dddsample/domain/model/location/UnLocode;Ljava/util/Date;)Lse/citerus/dddsample/domain/model/cargo/TrackingId;"));

        NodeSet nodeSet = new NodeSet();
        Node node1 = new ClassNode();
        node1.setName("se/citerus/dddsample/domain/model/handling/HandlingEvent");
        ((ClassNode) node1).setType(ClassNode.Type.NORMAL);
        Node node2 = new ClassNode();
        node2.setName("se/citerus/dddsample/application/BookingService");
        ((ClassNode) node2).setType(ClassNode.Type.NORMAL);
        nodeSet.addNode(node1);
        nodeSet.addNode(node2);
        Node node3 = new MethodNode();
        node3.setName("se/citerus/dddsample/application/util/SampleDataGenerator.loadHibernateData(Lorg/springframework/transaction/support/TransactionTemplate;Lorg/hibernate/SessionFactory;Lse/citerus/dddsample/domain/model/handling/HandlingEventFactory;Lse/citerus/dddsample/domain/model/handling/HandlingEventRepository;)V");
        Node node4 = new MethodNode();
        node4.setName("se/citerus/dddsample/infrastructure/persistence/hibernate/CargoRepositoryHibernate.find(Lse/citerus/dddsample/domain/model/cargo/TrackingId;)Lse/citerus/dddsample/domain/model/cargo/Cargo;");
        nodeSet.addNode(node3);
        nodeSet.addNode(node4);
        PairRelationInfo classEdges = new PairRelationInfo(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);
        PairRelation pairRelation1 = new PairRelation("1",1.0,node1,node2);
        classEdges.addRelation(pairRelation1);
        PairRelationInfo methodEdges = new PairRelationInfo(PairRelation.INFO_NAME_STATIC_METHOD_CALL_COUNT);
        PairRelation pairRelation2 = new PairRelation("1",1.0,node3,node4);
        methodEdges.addRelation(pairRelation2);

        javaData.setNodeSet(nodeSet);
        javaData.setClassEdges(classEdges);
        javaData.setMethodEdges(methodEdges);

        javaData.formatNodeSet();
        System.out.println("ending");


    }
}
