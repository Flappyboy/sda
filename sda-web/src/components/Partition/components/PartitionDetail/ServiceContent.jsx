import React, { Component } from 'react';
import { Table, Button, Pagination, Input, Grid, Icon, Form, Field } from '@alifd/next';
import emitter from '../ev';
import { queryEdge, queryNode, moveNode } from '../../../../api';

const FormItem = Form.Item;
const { Row, Col } = Grid;

const extractNames = (partitionDetail) => {
  if(!partitionDetail){
    return [];
  }
  let names = [];
  for(let node in partitionDetail.nodes){
    names.push(node);
  }
  return names;
}

export default class ServiceContent extends Component {
  static displayName = 'CustomTable';

  static propTypes = {};

  static defaultProps = {};

  componentWillReceiveProps(nextProps) {
    this.setState({
      partition: nextProps.partition,
      partitionNames: extractNames(nextProps.partitionDetail),
    })
  }

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
      pageSize: 11,
      total: 0,
      partition: props.partition,
      partitionNode: null,
      partitionNames: extractNames(props.partitionDetail),
      targetServiceName: '',
    };
    this.field = new Field(this);
    // this.queryNodeAndEdge('class');
  }

  move = (index, record) => {
    if(!this.state.targetServiceName in this.state.partitionNames)
      return;
    record.status = 0;
    this.setState({
      dataSource: this.state.dataSource,
    });
    const param = {
      id: this.state.partitionDetail.id,
      nodeId: record.id,
      oldPartitionResultName: this.state.data.name,
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
    })
    if(name in this.state.partitionNames){

    }
  };

  updatePartitionNode() {
    console.log(this.state.partitionNode);
  }

  renderOperator = (value, index, record) => {
    if (record.status === 0) {
      return (
        <div>
          <Icon type="loading" />
        </div>
      );
    }
    if(this.state.targetServiceName in this.state.partitionNames){
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
      return (
        <div style={styles.table}>
          {this.state.partitionNode == null ? null : (
            <Row wrap style={{marginBottom: 10}}>
              <Input size="small" label="Name :" />
              <Input size="small" label="Desc :" />
              <Button>保存</Button>
            </Row>

          )}

          <Row wrap style={{marginBottom: 10}}>
            <Col l="12">
              <Input addonTextBefore="目标服务"
                     size="medium"
                     onChange={this.targetServiceNameInputChange}/>
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
              <Pagination pageSize={this.state.pageSize} total={this.state.total} onChange={this.handleChange} />
            </div>
        </div>
      );
    }
    return (
      <div style={styles.table}>
        <Table dataSource={this.state.dataSource} hasBorder={false} loading={this.state.isLoading} >
          <Table.Column title="源节点" dataIndex="pair.sourceNodeObj.name" />
          <Table.Column title="目标节点" dataIndex="pair.targetNodeObj.name" />
          <Table.Column title="类型" dataIndex="pair.infoName" />
          <Table.Column title="值" dataIndex="pair.value" />
        </Table>
        <div style={styles.pagination}>
          <Pagination pageSize={this.state.pageSize} total={this.state.total} onChange={this.handleChange} />
        </div>
      </div>
    );
  }
  handleChange = (current) => {
    console.log(current);
    this.updateList(current);
  }
  updateList = (pageNum) => {
    let call = queryNode;
    if (this.state.dataType === 'edge') {
      call = queryEdge;
    }
    const queryParam = {
      id: this.state.data.id,
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
  }
  queryNodeAndEdge = (type, partitionNode) => {
    this.setState({
      dataType: type,
      partitionNode: partitionNode,
      data: partitionNode.data,
    });
    this.updateList(1, partitionNode.data);
  };
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
