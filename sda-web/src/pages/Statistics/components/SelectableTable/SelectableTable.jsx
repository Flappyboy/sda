import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import moment from 'moment';
import emitter from '../ev';
import AddDialog from './components/AddDialog';
import DeleteBalloon from './components/DeleteBalloon';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import { queryStatisticsList, queryStatistics, delStatistics, addStatistics, addPartition } from '../../../../api';
// 注意：下载数据的功能，强烈推荐通过接口实现数据输出，并下载
// 因为这样可以有下载鉴权和日志记录，包括当前能不能下载，以及谁下载了什么
const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';
export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};

  preprocess = (dataList) => {
    dataList.forEach(data => {
      data.createTime = moment(data.createdat).format(DATE_FORMAT);
      if (data.starttine)
        data.startTime = moment(data.starttine).format(DATE_FORMAT);
      if (data.endtime)
        data.endTime = moment(data.endtime).format(DATE_FORMAT);
      if (!('status' in data)) {
        data.status = true;
      }
    });
  }

  updateList = (pageNum, queryParam) => {
    // const queryParam = {
    //   pageSize: this.state.pageSize,
    //   page: pageNum,
    // };
    if (!queryParam) {
      queryParam = {};
    }
    if (this.state.app) {
      queryParam.appid = this.state.app.id;
    }
    queryParam.pageSize = this.state.pageSize;
    queryParam.page = pageNum;

    this.setState({
      isLoading: true,
    });
    queryStatisticsList(queryParam).then((response) => {
      console.log(response.data.data);
      this.preprocess(response.data.data.list);
      this.setState({
        dataSource: response.data.data.list,
        isLoading: false,
        total: response.data.data.total,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }
  handleChange = (current) => {
    console.log(current);
    this.updateList(current);
  }

  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('query_statistics', this.queryStatistics);

    this.updateList(1);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_statistics', this.queryStatistics);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      app: nextProps.app,
    });
    this.state.app = nextProps.app;
    this.updateList(1);
    emitter.emit('clear_statistics_detail');
  }

  constructor(props) {
    super(props);

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
      redirectToPartition: false,
      pageSize: 10,
      total: 0,
      app: props.app,
    };
  }

  queryStatistics = (param) => {
    console.log('query aha ', param);
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
    delStatistics(data[index].id).then((response) => {
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

  partition = (record) => {
    const data = this.state.dataSource;

    let target;
    data.forEach((item) => {
      if (item.id === record.id) {
        target = item;
      }
    });
    const param = {
      appid: target.appid,
      algorithmsid: 1,
      dynamicanalysisinfoid: target.id,
      type: 0,
    };
    addPartition(param).then((response) => {
      console.log(response.data.data);
      this.setState({
        // redirectToPartitionParam: id,
        redirectToPartition: true,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  };

  addNewStatistics = (values, callback) => {
    addStatistics(values).then((response) => {
      console.log(response.data.data);
      const data = this.state.dataSource;
      console.log(values);
      values.status = false;
      data.splice(0, 0, values);
      this.setState({
        dataSource: data,
      });
      callback();
    })
      .catch((error) => {
        console.log(error);
      });
  };

  queryDetail = (record) => {
    emitter.emit('query_statistics_detail', record.id);
  };

  renderOperator = (value, index, record) => {
    if (record.status !== 1) {
      return (
        <div>
          <Icon type="loading" />
        </div>
      );
    }
    return (
      <div>
        <a style={{ cursor: 'pointer' }} onClick={this.queryDetail.bind(this, record)}>Detail</a>
        {/* <a style={{ cursor: 'pointer', marginLeft: '10px' }} onClick={this.partition.bind(this, record)}>划分</a> */}
        <a style={{ cursor: 'pointer', marginLeft: '10px' }} onClick={this.deleteItem.bind(this, record)} >
          删除
        </a>
      </div>
    );
  };

  render() {
    let title = null;
    if (this.state.app) {
      title = `${this.state.app.name} 动态统计数据`;
    }
    if (this.state.redirectToPartition) {
      return (
        <Redirect to={{ pathname: '/partition', search: `?addAppId=${this.state.redirectToPartitionParam}` }} />
      );
    }
    return (
      <IceContainer title={title} className="selectable-table" style={styles.selectableTable}>
        <div style={styles.IceContainer}>
          <div>
            <AddDialog addNewStatistics={this.addNewStatistics} app={this.state.app} />
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
          <div>
            <a href="/" download>
              <Icon size="small" type="download" /> 导出表格数据到 .csv 文件
            </a>
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
            <Table.Column title="创建日期" dataIndex="createTime" width={150} />
            <Table.Column title="开始日期" dataIndex="startTime" width={150} />
            <Table.Column title="结束日期" dataIndex="endTime" width={160} />
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
