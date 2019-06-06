import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import moment from 'moment';
import emitter from '../ev';
import AddDialog from './components/AddDialog';
import DeleteBalloon from './components/DeleteBalloon';
import { queryPartitionList, queryPartition, delPartition, queryAppList } from '../../../../api';


const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

// 注意：下载数据的功能，强烈推荐通过接口实现数据输出，并下载
// 因为这样可以有下载鉴权和日志记录，包括当前能不能下载，以及谁下载了什么

export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};
  preprocess = (dataList) => {
    dataList.forEach(data => {
      data.createTime = moment(data.createdAt).format(DATE_FORMAT);
      if (!('status' in data)) {
        data.status = true;
      }
    });
  }

  updateList = (pageNum, callBack) => {
    const queryParam = {
      pageSize: this.state.pageSize,
      page: pageNum,
    };
    this.setState({
      isLoading: true,
    });
    queryPartitionList(queryParam).then((response) => {
      console.log(response.data.data);
      this.preprocess(response.data.data.list);
      this.setState({
        dataSource: response.data.data.list,
        isLoading: false,
        total: response.data.data.total,
      });
      if (callBack) {
        callBack();
      };
    })
      .catch((error) => {
        console.log(error);
      });
  }
  handleChange = (current) => {
    console.log(current);
    this.updateList(current);
  }

  getQueryString = (name) => {
    const reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    const r = this.props.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
  }

  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('query_partitions', this.queryPartition);
    // if (this.props.search) {

    //   this.addNewItem({
    //     id: 1004123120,
    //     statisticsId: this.getQueryString('addAppId'),
    //     status: false,
    //   });
    // } else {
    //   this.updateList(1);
    // }
    this.updateList(1);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_partitions', this.queryPartition);
  }

  constructor(props) {
    super(props);
    console.log(this.props);

    // 表格可以勾选配置项
    this.rowSelection = {
      // 表格发生勾选状态变化时触发
      onChange: (ids) => {
        console.log('ids', ids);
        this.setState({
          selectedRowKeys: ids,
        });
      },
      // 全选表格时触发的回调
      onSelectAll: (selected, records) => {
        console.log('onSelectAll', selected, records);
      },
      // 支持针对特殊行进行定制
      getProps: (record) => {
        return {
          disabled: record.status === false,
        };
      },
    };

    this.state = {
      selectedRowKeys: [],
      dataSource: [],
      pageSize: 10,
      total: 0,
    };
  }

  queryPartition = (param) => {
    console.log('query aha ', param);
    // emitter.emit('query_partition_detail', 'Hello');
    this.updateList(1);
  };

  clearSelectedKeys = () => {
    this.setState({
      selectedRowKeys: [],
    });
  };

  deleteSelectedKeys = () => {
    const { selectedRowKeys } = this.state;
    console.log('delete keys', selectedRowKeys);
  };

  deleteItem = (record) => {
    const { id } = record;
    console.log('delete item', id);

    const data = this.state.dataSource;
    console.log(record);
    const index = data.findIndex((item) => {
      return item.id === record.id;
    });
    data[index].status = false;
    this.setState({
      dataSource: data,
    });
    delPartition(data[index].id).then((response) => {
      console.log(response.data.data);

      if (index !== -1) {
        data.splice(index, 1);
      }
      this.setState({
        dataSource: data,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  };

  queryDetail = (record) => {
    const data = this.state.dataSource;

    data.forEach((item) => {
      if (item.id === record.id) {
        emitter.emit('query_partition_detail', item.id);
      }
    });
  };

  addNewItem = (values) => {
    this.updateList(1, () => {
      // const data = this.state.dataSource;
      // console.log(values);
      // values.status = false;
      // data.splice(0, 0, values);
      // this.setState({
      //   dataSource: data,
      // });
    });
  };

  renderOperator = (value, index, record) => {
    if (!record.status) {
      return (
        <div>
          <Icon type="loading" />
        </div>
      );
    }
    return (
      <div>
        <a style={{ cursor: 'pointer' }} onClick={this.queryDetail.bind(this, record)}>Detail</a>
        <a style={{ cursor: 'pointer', marginLeft: '10px' }} onClick={this.deleteItem.bind(this, record)} >
          删除
        </a>
      </div>
    );
  };

  render() {
    return (
      <IceContainer className="selectable-table" style={styles.selectableTable}>
        <div style={styles.IceContainer}>
          <div>
            {/* <AddDialog addNewStatistics={this.addNewStatistics} /> */}
            {/* <Button onClick={this.addStatistics} size="small" style={styles.batchBtn}>
              <Icon type="add" />增加
            </Button> */}
            <Button
              onClick={this.deleteSelectedKeys}
              size="small"
              style={styles.batchBtn}
              disabled={!this.state.selectedRowKeys.length}
            >
              <Icon type="ashbin" />删除
            </Button>
            <Button
              onClick={this.clearSelectedKeys}
              size="small"
              style={styles.batchBtn}
            >
              <Icon type="close" />Clear
            </Button>
          </div>
        </div>
        <div>
          <Table
            dataSource={this.state.dataSource}
            isLoading={this.state.isLoading}
            rowSelection={{
              ...this.rowSelection,
              selectedRowKeys: this.state.selectedRowKeys,
            }}
          >
            <Table.Column title="编码" dataIndex="id" width={120} />
            <Table.Column title="应用" dataIndex="appName" width={120} />
            <Table.Column title="类型" dataIndex="typeName" width={120} />
            <Table.Column title="统计编码" dataIndex="dynamicAnalysisInfoId" width={120} />
            <Table.Column title="创建日期" dataIndex="createTime" width={150} />
            <Table.Column title="算法" dataIndex="algorithmsName" width={150} />
            <Table.Column title="描述" dataIndex="desc" width={160} />
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
      </IceContainer>
    );
  }
}

const styles = {
  batchBtn: {
    marginRight: '10px',
  },
  IceContainer: {
    marginBottom: '20px',
    minHeight: 'auto',
    display: 'flex',
    justifyContent: 'space-between',
  },
  removeBtn: {
    marginLeft: 10,
  },
  pagination: {
    textAlign: 'right',
    paddingTop: '26px',
  },
};
