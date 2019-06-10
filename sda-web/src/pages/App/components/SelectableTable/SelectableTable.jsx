import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import moment from 'moment';
import { queryAppList, queryApp, delApp, delApps, addPartition } from '../../../../api';
import emitter from '../ev';
import AppDialog from './components/AppDialog';
import AddAppDialog from './components/AddAppDialog';
import EditAppDialog from './components/EditAppDialog';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import ConfirmDialogBtn from "../../../../components/Dialog/ConfirmDialogBtn";

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';
export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};

  preprocess = (dataList) => {
    dataList.forEach(data => {
      this.preprocessData(data);
    });
    return dataList;
  }
  preprocessData = (data) => {
    data.createTime = moment(data.createdAt).format(DATE_FORMAT);
    if (!data.status) {
      data.status = true;
    };
    return data;
  }

  updateList = (pageNum, app) => {
    if(pageNum <=0 ){
      pageNum = 1;
    }
    this.setState({
      currentPage: pageNum,
    });
    const queryParam = {
      name: app ? app.name: null,
      desc: app ? app.desc: null,
      pageSize: this.state.pageSize,
      pageNum: pageNum,
    };
    this.setState({
      isLoading: true,
    });
    queryAppList(queryParam).then((response) => {
      this.preprocess(response.data.result);
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
    this.eventEmitter = emitter.addListener('query_apps', this.queryApps);

    this.updateList(this.state.currentPage);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_apps', this.queryApps);
  }

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
      selectedRowKeys: [],
      dataSource: [],
      redirectToPartition: false,
      pageSize: 10,
      total: 0,
      currentPage: 1,
      updateAppDialogVisible: false,
      updateApp: null,
    };
  }

  queryApps = (param) => {
    this.updateList(1, param);
  };

  clearSelectedKeys = () => {
    this.setState({
      selectedRowKeys: [],
    });
  };

  deleteSelectedKeys = (callback) => {
    const { selectedRowKeys } = this.state;
    console.log('delete keys', selectedRowKeys);
    delApps(selectedRowKeys).then((response) => {
      this.clearSelectedKeys();
      if(selectedRowKeys.length == this.state.dataSource.length) {
        this.updateList(this.state.currentPage - 1);
      }else{
        this.updateList(this.state.currentPage);
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

    const data = this.state.dataSource;
    console.log(record);
    const index = data.findIndex((item) => {
      return item.id === record.id;
    });
    data[index].status = 0;
    this.setState({
      dataSource: data,
    });
    delApp(data[index].id).then((response) => {
      if (index !== -1) {
        data.splice(index, 1);
      }
      this.setState({
        dataSource: data,
      });
      if (callback){
        callback();
      }
    })
      .catch((error) => {
        console.log(error);
      });
  };

  partition = (record) => {
    const data = this.state.dataSource;

    let id;
    data.forEach((item) => {
      if (item.id === record.id) {
        id = item.id;
      }
    });
    const param = {
      appid: id,
      algorithmsid: 1,
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

  addNewItem = (values) => {
    const data = this.state.dataSource;
    data.splice(0, 0, this.preprocessData(values));
    this.setState({
      dataSource: data,
    });
  };
  handleChange = (current) => {
    console.log(current);
    this.setState({
      currentPage: current,
    });
    this.updateList(current);
  }
  showAttach = (target, record) => {
    const data = this.state.dataSource;

    let app = null;
    data.forEach((item) => {
      if (item.id === record.id) {
        app = item;
      }
    });
    emitter.emit('show_attach', target, app);
  }

  showDetail = (record) => {
    this.props.redirect("/appDetail",{id: record.id});
  };

  updateApp = (app) => {
    const data = this.state.dataSource;
    for(let i=0; i<data.length; i++){
      if (data[i].id === app.id) {
        data[i] = this.preprocessData(app);
        break;
      }
    }
    this.setState({
      dataSource: data,
    });
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
        <a style={styles.operate} >
          <EditAppDialog app={record} editCallback={this.updateApp.bind(this)}/>
        </a>

          <ConfirmDialogBtn title="Confirm" content="Confirm Delete！" onOk={this.deleteItem.bind(this, record)}>
            <a style={styles.operate} >
              Delete
            </a>
          </ConfirmDialogBtn>
      </div>
    );
  };

  closeAppDialog = () => {
    this.setState({
      updateAppDialogVisible: false
    });
  }

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
            <AddAppDialog addNewItem={this.addNewItem} />
            {/* <Button onClick={this.addStatistics} size="small" style={styles.batchBtn}>
              <Icon type="add" />增加
            </Button> */}

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
            dataSource={this.state.dataSource}
            loading={this.state.isLoading}
            rowSelection={{
              ...this.rowSelection,
              selectedRowKeys: this.state.selectedRowKeys,
            }}
          >
            <Table.Column title="ID" dataIndex="id" width={130} />
            <Table.Column title="App" dataIndex="name" width={110} />
            <Table.Column title="CreateTime" dataIndex="createTime" width={140} />
            {/*<Table.Column title="Node Count" dataIndex="nodeCount" width={80} />*/}
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
        <AppDialog visible={this.state.updateAppDialogVisible} close={this.closeAppDialog.bind(this)} type="update" updateCallback={this.updateApp.bind(this)} app={this.state.updateApp}/>
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
