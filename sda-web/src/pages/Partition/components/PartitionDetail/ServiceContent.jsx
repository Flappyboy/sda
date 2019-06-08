import React, { Component } from 'react';
import { Table, Pagination, Input, Grid, Icon } from '@alifd/next';
import emitter from '../ev';
import { queryEdge, queryNode, moveNode, moveNodes } from '../../../../api';

const { Row, Col } = Grid;

const extractNames = (partition) => {
  if(!partition){
    return [];
  }
  let names = [];
  for(let node in partition.nodes){
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
      partitionNames: extractNames(nextProps.partition),
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
      selectedRowKeys: [],
      dataSource: [],
      isLoading: true,
      pageSize: 11,
      total: 0,
      partition: props.partition,
      partitionNames: extractNames(props.partition),
      targetServiceName: '',

    };
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
      oldPartitionResultName: this.state.data.name,
      targetPartitionResultName: this.state.targetServiceName,
    };

    moveNode(param).then((response) => {
      this.state.dataSource.splice(index, 1);
      this.setState({
        dataSource: this.state.dataSource,
        selectedRowKes: [],
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


  };

  targetServiceNameInputChange = (name) => {
    this.setState({
        targetServiceName: name,
    })
    if(name in this.state.partitionNames){

    }
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
      return (
        <div style={styles.table}>
          <Row wrap>
            <Col l="12">
              <Input addonTextBefore="Target Server"
                     size="medium"
                     onChange={this.targetServiceNameInputChange}/>
            </Col>
          </Row>

          <Table dataSource={this.state.dataSource}
                 hasBorder={false}
                 loading={this.state.isLoading}
                 >
            <Table.Column title="类名" dataIndex="simpleName" />
            <Table.Column title="包名" dataIndex="packageName" />
            <Table.Column
              title="操作"
              cell={this.renderOperator}
              lock="right"
              width={120}
            />
          </Table>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} total={this.state.total} onChange={this.handleChange} />
          </div>
        </div>
      );
    }
    return (
      <div style={styles.table}>
        <Table dataSource={this.state.dataSource} hasBorder={false} loading={this.state.isLoading}>
          <Table.Column title="调用类" dataIndex="caller.simpleName" />
          <Table.Column title="被调类" dataIndex="callee.simpleName" />
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
        dataSource: response.data.data.list,
        isLoading: false,
        total: response.data.data.total,
      });
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });
  }
  queryNodeAndEdge = (type, data) => {
    this.setState({
      dataType: type,
      data,
    });
    this.updateList(1, data);
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
