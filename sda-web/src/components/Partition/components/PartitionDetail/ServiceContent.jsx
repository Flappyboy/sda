import React, { Component } from 'react';
import { Table, Button, Pagination, Input, Grid, Icon, Form, Field } from '@alifd/next';
import emitter from '../ev';
import { queryEdge, queryNode, moveNode, moveNodes, addPartitionNode, delPartitionNode, putPartitionNode } from '../../../../api';
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
      selectedRowKeys: [],
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

  rowSelection = {
    // 表格发生勾选状态变化时触发
    onChange: (ids) => {
      // console.log('ids', ids);
      this.setState({
        selectedRowKeys: ids,
      });
    },
    // 全选表格时触发的回调
    onSelectAll: (selected, records) => {
      // console.log('onSelectAll', selected, records);
    },
    // 支持针对特殊行进行定制
    getProps: (record) => {
      return {
        disabled: false,
      };
    },
  };

  oldName = null;

  moveNodes = (callback) => {
    if(!this.state.targetServiceName in this.state.partitionNames)
      return;
    this.setState({
      dataSource: this.state.dataSource,
    });
    const param = {
      id: this.state.partition.id,
      oldPartitionResultName: this.state.partitionNode.name,
      targetPartitionResultName: this.state.targetServiceName,
    };

    const nodes = this.state.selectedRowKeys;

    moveNodes(param, nodes).then((response) => {
      this.updateList(1);
      /*const operates ={
        putEdges: {
          '190324D9G3SXHZ2W': {
            data: 1
          }
        }
      };*/
      this.setState({
        selectedRowKeys: [],
      });
      emitter.emit('partition_detail_operate', response.data.data);
      emitter.emit('query_partition_detail_evaluate');
      callback();
    })
      .catch((error) => {
        console.log(error);
      });
  };

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
        selectedRowKeys: [],
      });

      /*const operates ={
        putEdges: {
          '190324D9G3SXHZ2W': {
            data: 1
          }
        }
      };*/
      emitter.emit('partition_detail_operate', response.data.data);
      emitter.emit('query_partition_detail_evaluate');

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
      selectedRowKeys: [],
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
          <a style={{ cursor: 'pointer' }} onClick={this.move.bind(this, index, record)}>Move</a>
        </div>
      );
    }
    return (
      <div>
        <a style={{ cursor: 'pointer', opacity: 0.2 }} >Move</a>
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
            {this.state.saveLoading? "Saving......" : "Save"}
          </Button>)
      }

      let deleteBtn = null;
      if(this.state.total == 0) {
        deleteBtn = (
          <ConfirmDialogBtn title="Delete this Server" content="Confirm delete this Server" onOk={this.delPartitionNode}>
            <Button style={{marginLeft: 10}}>Delete Service</Button>
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
              <Input addonTextBefore="Target Service"
                     size="medium"
                     onChange={this.targetServiceNameInputChange}/>
            </Col>
            <Col l="12">
              {(this.state.partitionNames[this.state.targetServiceName] || this.state.targetServiceName==null || this.state.targetServiceName.length == 0) ? null:
                <ConfirmDialogBtn title="Add Service" content="Confirm Add Service" onOk={this.addPartitionNode}>
                  <Button style={{marginLeft: 10}} >Add Service</Button>
                </ConfirmDialogBtn>
              }
              {(!(this.state.selectedRowKeys.length>0 &&this.state.partitionNames[this.state.targetServiceName] && this.state.partitionNode.name !== this.state.targetServiceName)) ? null:
                <ConfirmDialogBtn title="Move Nodes" content="Confirm Move Nodes" onOk={this.moveNodes.bind(this)}>
                  <Button style={{marginLeft: 10}} >Move Nodes To Service</Button>
                </ConfirmDialogBtn>
              }
            </Col>
          </Row>
          <Row wrap style={{height: 550}}>
            <Table
              dataSource={this.state.dataSource}
              hasBorder={false}
              loading={this.state.isLoading}
              rowSelection={{
                ...this.rowSelection,
                selectedRowKeys: this.state.selectedRowKeys,
              }}
              maxBodyHeight={500}
              fixedHeader
              stickyHeader={false}>
              <Table.Column title="NodeName" dataIndex="name" />
              <Table.Column title="Class" dataIndex="clazz" />
              <Table.Column title="Attrs" dataIndex="attrs" />
              <Table.Column
                title="Operate"
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
            <Table.Column title="Source" dataIndex="pair.sourceNodeObj.name"/>
            <Table.Column title="Target" dataIndex="pair.targetNodeObj.name"/>
            <Table.Column title="Type" dataIndex="pair.infoName"/>
            <Table.Column title="Value" dataIndex="pair.value"/>
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
