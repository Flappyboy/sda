import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import moment from 'moment';
import { queryInfos, delInfos, downloadInfo, downloadInfoConfirm } from '../../../../api';
import emitter from '../ev';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import ConfirmDialogBtn from "../../../../components/Dialog/ConfirmDialogBtn";
import {formatDateForDataList} from "../../../../utils/preprocess";

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';
export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);

    // 表格可以勾选配置项
    this.rowSelection = {
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

    this.state = {
      app: props.app,
      selectedRowKeys: [],
      dataSource: [],
      tableDataSource: [],
      pageSize: 10,
      total: 0,
      currentPage: 1,
    };
  }


  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('query_infos', this.queryInfos);

    this.updateList(this.state.currentPage);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_infos', this.queryInfos);
  }


  updateList = (pageNum) => {
    if(pageNum <=0 ){
      pageNum = 1;
    }
    this.setState({
      currentPage: pageNum,
      tableDataSource: this.state.dataSource.slice((pageNum-1)*this.state.pageSize, pageNum*this.state.pageSize),
    });

  };

  queryInfos = (info, page) => {
    this.info = info;

    this.setState({
      selectedRowKeys: [],
      dataSource: [],
      tableDataSource: [],
      currentPage: 1,
    });

    const queryParam = {
      appId: this.state.app.id,
      name: info.name,
    };
    this.setState({
      isLoading: true,
    });
    queryInfos(queryParam).then((response) => {
      formatDateForDataList(response.data);
      this.state.dataSource = response.data;
      this.state.total = response.data.length;
      this.setState({
        isLoading: false,
      });
      this.updateList(page? page: 1);
    })
      .catch((error) => {
        console.log(error);
      });
  };

  clearSelectedKeys = () => {
    this.setState({
      selectedRowKeys: [],
    });
  };

  deleteSelectedKeys = (callback) => {
    const { selectedRowKeys } = this.state;
    console.log('delete keys', selectedRowKeys);
    delInfos(this.info.name, selectedRowKeys).then((response) => {
      this.clearSelectedKeys();
      if(selectedRowKeys.length == this.state.dataSource.length) {
        this.queryInfos(this.info, this.state.currentPage - 1);
      }else{
        this.updateList(this.info, this.state.currentPage);
      }
      if (callback){
        callback();
      }
    })
      .catch((error) => {
        console.log(error);
      });
  };

  deleteItem = (record, callback) => {
    const { id } = record;
    console.log('delete item', id);

    const data = this.state.tableDataSource;
    console.log(record);
    const index = data.findIndex((item) => {
      return item.id === record.id;
    });
    data[index].status = 0;
    this.setState({
      tableDataSource: data,
    });
    delInfos(this.info.name, [data[index].id]).then((response) => {
      if (index !== -1) {
        data.splice(index, 1);
      }
      this.setState({
        tableDataSource: data,
      });
      if (callback){
        callback();
      }
    })
      .catch((error) => {
        console.log(error);
      });
  };

  download(record) {
    const params = {
      name: this.info.name,
      id: record.id,
    };
    downloadInfoConfirm(params).then((response) => {
      downloadInfo(params);
    })
      .catch((error) => {
        console.log(error);
        alert(error);
      });
  }

  handleChange = (current) => {
    console.log(current);
    this.setState({
      currentPage: current,
    });
    this.updateList(current);
  };

  renderOperator = (value, index, record) => {
    let download = null;
    if (this.info.name == "SYS_PINPOINT_PLUGIN_INFO") {
      download = <a style={styles.operate} onClick={this.download.bind(this, record)}>
        Download
      </a>
    }
    return (
      <div>

        {download}

          <ConfirmDialogBtn title="Confirm" content="Confirm Delete！" onOk={this.deleteItem.bind(this, record)}>
            <a style={styles.operate} >
              Delete
            </a>
          </ConfirmDialogBtn>
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
        <div style={styles.IceContainer}>
          <div>
              <ConfirmDialogBtn title="Confirm" content="Confirm Delete！" onOk={this.deleteSelectedKeys.bind(this)} >
                <Button
                  size="small"
                  style={styles.batchBtn}
                  disabled={!this.state.selectedRowKeys.length}
                >
                  <Icon type="ashbin" />
                  Delete
                </Button>
              </ConfirmDialogBtn>

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
            dataSource={this.state.tableDataSource}
            loading={this.state.isLoading}
            rowSelection={{
              ...this.rowSelection,
              selectedRowKeys: this.state.selectedRowKeys,
            }}
          >
            <Table.Column title="ID" dataIndex="id" width={130} />
            <Table.Column title="App" dataIndex="name" width={110} />
            <Table.Column title="CreateTime" dataIndex="createTime" width={140} />
            <Table.Column title="Status" dataIndex="status" width={80} />
            <Table.Column title="Description" dataIndex="desc" width={140} />
            <Table.Column
              title="Operate"
              cell={this.renderOperator}
              lock="right"
              width={200}
            />
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
