import React, { Component } from 'react';
import { Table, Button, Pagination, Input, Grid, Icon, Form, Field } from '@alifd/next';
import emitter from '../ev';
import { queryEdge, queryNode, moveNode, addPartitionNode, delPartitionNode, putPartitionNode } from '../../../../api';
import ConfirmDialogBtn from "../../../Dialog/ConfirmDialogBtn";

const FormItem = Form.Item;
const { Row, Col } = Grid;

const extractNames = (partitionDetail) => {
  if(!partitionDetail){
    return {};
  }
  let names = {};
  partitionDetail.nodes.map((node) => {
    names[node.name] =true;
  });
  return names;
}

export default class ServiceContent extends Component {
  static displayName = 'CustomTable';

  static propTypes = {};

  static defaultProps = {};

  /*componentWillReceiveProps(nextProps) {
    this.setState({
      partition: nextProps.partition,
      partitionNames: extractNames(nextProps.partitionDetail),
    })
  }*/

  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('query_partition_detail_ne', this.queryNodeAndEdge);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_partition_detail_ne', this.queryNodeAndEdge);
  }

  constructor(props) {
    super(props);
    this.state = {
      init: true,
      dataSource: [],
      isLoading: true,
      saveLoading: false,
      pageSize: 11,
      page: 1,
      total: 0,
      partition: props.partition,
      partitionNode: null,
      partitionNames: extractNames(props.partitionDetail),
      targetServiceName: '',
      isEdit: false,
    };
    this.field = new Field(this);
    // this.queryNodeAndEdge('class');
  }

  oldName = null;

  move = (index, record) => {
    if(!this.state.targetServiceName in this.state.partitionNames)
      return;
    record.status = 0;
    this.setState({
      dataSource: this.state.dataSource,
    });
    const param = {
      id: this.state.partition.id,
      nodeId: record.id,
      oldPartitionResultName: this.state.partitionNode.name,
      targetPartitionResultName: this.state.targetServiceName,
    };

    moveNode(param).then((response) => {
      this.state.dataSource.splice(index, 1);
      this.setState({
        dataSource: this.state.dataSource,
      });

      /*const operates ={
        putEdges: {
          '190324D9G3SXHZ2W': {
            data: 1
          }
        }
      };*/
      emitter.emit('partition_detail_operate', response.data.data);
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });


  }

  targetServiceNameInputChange = (name) => {
    this.setState({
        targetServiceName: name,
    });
    if(name in this.state.partitionNames){

    }
  };

  edit(){
    this.setState({
        isEdit: true,
    })
  }
  notEdit(){
    this.setState({
      isEdit: false,
    })
  }


  handleChange = (current) => {
    console.log(current);
    this.updateList(current);
  };
  updateList = (pageNum) => {
    let call = queryNode;
    if (this.state.dataType === 'edge') {
      call = queryEdge;
    }
    this.setState({
      page: pageNum,
    });
    const queryParam = {
      id: this.state.partitionNode.id,
      pageSize: this.state.pageSize,
      page: pageNum,
    };
    this.setState({
      init: false,
      isLoading: true,
    });
    call(queryParam).then((response) => {
      this.setState({
        dataSource: response.data.result,
        isLoading: false,
        total: response.data.total,
      });
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });
  };
  queryNodeAndEdge = (type, partitionNode) => {
    this.state.partitionNode = partitionNode;
    this.oldName = partitionNode.name;
    this.state.dataType = type;
    this.setState({
      dataType: type,
      page: 1,
      partitionNode: partitionNode,
    });
    this.updateList(1, partitionNode);
  };

  updatePartitionNode() {
    const node = {
      id: this.state.partitionNode.id,
      name: this.state.partitionNode.name,
      desc: this.state.partitionNode.desc,
    };
    this.setState({
      saveLoading: true,
    });
    putPartitionNode(node).then((response) => {
      emitter.emit('partition_detail_operate', {
        putNodes: [node],
      });
      if(this.oldName != null)
        this.state.partitionNames[this.oldName] = false;
      this.oldName = node.name;
      this.state.partitionNames[node.name] = true;
      this.setState({
        partitionNames: Object.assign({}, this.state.partitionNames),
      });
      this.setState({
        saveLoading: false,
      });
    })
      .catch((error) => {
        console.log(error);
        close(error);
        this.setState({
          saveLoading: false,
        });
      });


  }

  addPartitionNode = (close) => {

    const nodes = {};
    const node ={
      partitionId: this.state.partition.id,
      name: this.state.targetServiceName,
      desc: "",
      size: 0,
    };
    addPartitionNode(node).then((response) => {
      node.id = response.data.id;
      this.queryNodeAndEdge("node", node);
      emitter.emit('partition_detail_operate', {
        addNodes: [node],
      });
      emitter.emit('partition_detail_operate', {
        selectNode: node,
      });
      this.state.partitionNames[this.state.targetServiceName] = true;
      this.setState({
        partitionNames: Object.assign({}, this.state.partitionNames),
      });
      close();
    })
      .catch((error) => {
        console.log(error);
        close(error);
      });
  };

  delPartitionNode = (close) => {

    const node = this.state.partitionNode;

    delPartitionNode(node.id).then((response) => {
      emitter.emit('partition_detail_operate', {
        deleteNodes: [node],
      });
      this.state.partitionNames[this.state.targetServiceName] = false;
      this.setState({
        dataType: null,
        partitionNode: node,
      });
      this.setState({
        partitionNames: Object.assign({}, this.state.partitionNames),
      });
      // this.forceUpdate()
      close();
    })
      .catch((error) => {
        console.log(error);
        close(error);
      });
  };

  renderOperator = (value, index, record) => {
    if (record.status === 0) {
      return (
        <div>
          <Icon type="loading" />
        </div>
      );
    }
    if(this.state.partitionNames[this.state.targetServiceName] && this.state.partitionNode.name !== this.state.targetServiceName){
      return (
        <div>
          <a style={{ cursor: 'pointer' }} onClick={this.move.bind(this, index, record)}>移动</a>
        </div>
      );
    }
    return (
      <div>
        <a style={{ cursor: 'pointer', opacity: 0.2 }} >移动</a>
      </div>
    );
  };

  render() {
    if (this.state.init) {
      return (
        <div />
      );
    }
    if (this.state.dataType === 'node') {

      let edit = null;
      let btn = null;
      if(this.state.partitionNode.name != null && this.state.partitionNode.name.length != 0
        && this.state.partitionNames[this.state.partitionNode.name] != true){
        btn = (
          <Button style={{marginLeft: 10}} onClick={this.updatePartitionNode.bind(this)}>
            {this.state.saveLoading? "保存中..." : "保存"}
          </Button>)
      }

      let deleteBtn = null;
      if(this.state.total == 0) {
        deleteBtn = (
          <ConfirmDialogBtn title="删除划分节点" content="确认删除该划分节点" onOk={this.delPartitionNode}>
            <Button style={{marginLeft: 10}}>删除服务</Button>
          </ConfirmDialogBtn>
        );
      }
      if(this.state.partitionNode != null) {
        if (!this.state.isEdit) {
          edit = (
            <Row wrap style={{marginBottom: 10}}>
              <Input label="Name :" onFocus={this.edit.bind(this)} value={this.state.partitionNode.name}/>
              <Input style={{marginLeft: 10}} label="Desc :" onFocus={this.edit.bind(this)} value={this.state.partitionNode.desc}/>
              {btn}
              {deleteBtn}
            </Row>
          );
        } else {
          edit = (
            <Row wrap style={{marginBottom: 10}}>
              <Input label="Name :"
                     onBlur={this.notEdit.bind(this)}
                     onChange={(value) => {this.state.partitionNode.name = value; this.setState({})}}
                     defaultValue={this.state.partitionNode.name}
                     trim/>
              <Input style={{marginLeft: 10}}
                     label="Desc :"
                     onBlur={this.notEdit.bind(this)}
                     onChange={(value) => {this.state.partitionNode.desc = value; this.setState({})}}
                     defaultValue={this.state.partitionNode.desc}
                     trim/>
                {btn}
              {deleteBtn}
            </Row>
          );
        }
      }
      return (
        <div style={styles.table}>
          {edit}
          <Row wrap style={{marginBottom: 10}}>
            <Col l="12">
              <Input addonTextBefore="目标服务"
                     size="medium"
                     onChange={this.targetServiceNameInputChange}/>
            </Col>
            <Col l="12">
              {(this.state.partitionNames[this.state.targetServiceName] || this.state.targetServiceName==null || this.state.targetServiceName.length == 0) ? null:
                <ConfirmDialogBtn title="新增节点" content="确认新增节点" onOk={this.addPartitionNode}>
                  <Button style={{marginLeft: 10}} >新增服务</Button>
                </ConfirmDialogBtn>
              }
            </Col>
          </Row>
          <Row wrap style={{height: 550}}>
            <Table dataSource={this.state.dataSource} hasBorder={false} loading={this.state.isLoading} maxBodyHeight={500} fixedHeader stickyHeader={false}>
              <Table.Column title="节点名" dataIndex="name" />
              <Table.Column title="类型" dataIndex="clazz" />
              <Table.Column title="属性" dataIndex="attrs" />
              <Table.Column
                title="操作"
                cell={this.renderOperator}
                lock="right"
                width={120}
              />
            </Table>
          </Row>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} current={this.state.page} total={this.state.total} onChange={this.handleChange} />
          </div>
        </div>
      );
    }
    if (this.state.dataType === 'edge') {
      return (
        <div style={styles.table}>
          <Row wrap style={{height: 600}}>
          <Table dataSource={this.state.dataSource} hasBorder={false} loading={this.state.isLoading} maxBodyHeight={550} fixedHeader stickyHeader={false}>
            <Table.Column title="源节点" dataIndex="pair.sourceNodeObj.name"/>
            <Table.Column title="目标节点" dataIndex="pair.targetNodeObj.name"/>
            <Table.Column title="类型" dataIndex="pair.infoName"/>
            <Table.Column title="值" dataIndex="pair.value"/>
          </Table>
          </Row>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} current={this.state.page} total={this.state.total} onChange={this.handleChange}/>
          </div>
        </div>
      );
    }
    return null;
  }
}
const styles = {
  pagination: {
    textAlign: 'right',
    paddingTop: '26px',
  },
  table: {
    margin: 20,

  },
};
