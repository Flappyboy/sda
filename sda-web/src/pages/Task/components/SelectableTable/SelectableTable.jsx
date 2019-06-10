import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import moment from 'moment';
import { queryTasks } from '../../../../api';
import emitter from '../ev';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import ConfirmDialogBtn from "../../../../components/Dialog/ConfirmDialogBtn";
import {formatDateForDataList} from "../../../../utils/preprocess";

export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};

  updateList = (pageNum, obj) => {
    if(pageNum <=0 ){
      pageNum = 1;
    }
    this.setState({
      currentPage: pageNum,
    });
    const queryParam = {
      appName: obj ? obj.appName: null,
      type: obj ? obj.type: null,
      pageSize: this.state.pageSize,
      pageNum: pageNum,
    };
    this.setState({
      isLoading: true,
    });
    queryTasks(queryParam).then((response) => {
      formatDateForDataList(response.data.result, "startTime", "startTimeFormat");
      formatDateForDataList(response.data.result, "endTime", "endTimeFormat");

      this.setState({
        dataSource: response.data.result,
        isLoading: false,
        total: response.data.total ? response.data.total : 10,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  };

  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('query_tasks', this.queryTasks);

    this.updateList(this.state.currentPage);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_tasks', this.queryTasks);
  }

  constructor(props) {
    super(props);
    this.state = {
      selectedRowKeys: [],
      dataSource: [],
      redirectToPartition: false,
      pageSize: 10,
      total: 0,
      currentPage: 1,
    };
  }

  queryTasks = (param) => {
    this.updateList(1, param);
  };

  handleChange = (current) => {
    this.setState({
      currentPage: current,
    });
    this.updateList(current);
  };

  showDetail() {

  }

  stop(record) {

  }

  renderOperator = (value, index, record) => {
    /*if (record.status !== 1) {
      return (
        <div>
          <Icon type="loading" />
        </div>
      );
    }*/
    return (
      <div>
        <a style={{...styles.operate, marginLeft: '0px'}} onClick={this.showDetail.bind(this, record)} >Detail</a>
        {/*<a style={{...styles.operate}} onClick={this.stop.bind(this, record)} >终止</a>*/}
      </div>
    );
  };

  render() {
    if (this.state.redirectToPartition) {
      return (
        <Redirect to={{ pathname: '/partition', search: `?addAppId=${this.state.redirectToPartitionParam}` }} />
      );
    }
    return (
      <IceContainer className="selectable-table" style={styles.selectableTable}>
        <div>
          <Table
            dataSource={this.state.dataSource}
            loading={this.state.isLoading}
          >
            <Table.Column title="ID" dataIndex="id" width={120} />
            <Table.Column title="App" dataIndex="appName" width={90} />
            <Table.Column title="Function Type" dataIndex="type" width={80} />
            <Table.Column title="Function Name" dataIndex="functionName" width={100} />
            <Table.Column title="Start Time" dataIndex="startTimeFormat" width={100} />
            <Table.Column title="End Time" dataIndex="endTimeFormat" width={100} />
            <Table.Column title="Status" dataIndex="status" width={80} />
            {/*<Table.Column
              title="Operate"
              cell={this.renderOperator}
              lock="right"
              width={200}
            />*/}
          </Table>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} current={this.state.currentPage} total={this.state.total} onChange={this.handleChange} />
          </div>
        </div>
      </IceContainer>
    );
  }
}

const styles = {
  operate: {
    cursor: 'pointer',
    marginLeft: '10px',
  },
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
