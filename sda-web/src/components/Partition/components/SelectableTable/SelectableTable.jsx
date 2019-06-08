import React, { Component } from 'react';
import { Table, Button, Icon, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import emitter from '../ev';
import { queryPartitionList, delPartition, } from '../../../../api';
import {AddTaskDialogBtn} from "../../../AddTask";
import ConfirmDialogBtn from "../../../../components/Dialog/ConfirmDialogBtn";
import EditPartitionDialogBtn from "./components/EditPartitionDialogBtn";
import {formatDateForData, formatDateForDataList} from "../../../../utils/preprocess";

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

export default class SelectableTable extends Component {
  static displayName = 'SelectableTable';

  static propTypes = {};

  static defaultProps = {};

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
      app: props.app,
      selectedRowKeys: [],
      dataSource: [],
      pageSize: 10,
      total: 0,
    };
  }

  update() {
    this.updateList(1)
  }

  updateList = (pageNum) => {
    const queryParam = {
      pageSize: this.state.pageSize,
      page: pageNum,
      appId: this.state.app.id,
      ...this.params
    };
    this.setState({
      isLoading: true,
    });
    queryPartitionList(queryParam).then((response) => {
      formatDateForDataList(response.data.result);
      this.setState({
        dataSource: response.data.result,
        isLoading: false,
        total: response.data.total,
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


  queryPartition = (param) => {
    console.log('query aha ', param);
    // emitter.emit('query_partition_detail', 'Hello');
    this.params = param;
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
        emitter.emit('query_partition_detail', Object.assign({}, item));
      }
    });
  };

  compareEvaluation = () => {
    const { selectedRowKeys } = this.state;
    if(selectedRowKeys.length == 0){
      return ;
    }
    emitter.emit("evaluation_compare", selectedRowKeys);
  };

  updatePartition = (obj) => {
    const data = this.state.dataSource;
    for(let i=0; i<data.length; i++){
      if (data[i].id === obj.id) {
        data[i] = formatDateForData(obj);
        break;
      }
    }
    this.setState({
      dataSource: data,
    });
  }

  addNewItem = (values) => {
    this.updateList(1);
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
        <a style={{...styles.operate, marginLeft: '0px'}} onClick={this.queryDetail.bind(this, record)}>Detail</a>

          <EditPartitionDialogBtn obj={record} editCallback={this.updatePartition.bind(this)}>
            <a style={styles.operate}>Edit</a>
          </EditPartitionDialogBtn>

          <ConfirmDialogBtn btnTitle="Delete" title="Confirm" content="Confirm Delete！" onOk={this.deleteItem.bind(this, record)}>
            <a style={styles.operate} >
              Delete
            </a>
          </ConfirmDialogBtn>
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
            <AddTaskDialogBtn app={this.state.app} type="Partition" onComplete={this.update.bind(this)}>
              <Button type="primary" size="small" style={styles.batchBtn}>
                <Icon type="add" />Add
              </Button>
            </AddTaskDialogBtn>
            {/*<Button
              onClick={this.deleteSelectedKeys}
              size="small"
              style={styles.batchBtn}
              disabled={!this.state.selectedRowKeys.length}
            >
              <Icon type="ashbin" />Delete
            </Button>*/}

            <Button
              onClick={this.compareEvaluation}
              size="small"
              style={styles.batchBtn}
              disabled={!this.state.selectedRowKeys.length}
            >
              Evaluations
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
            loading={this.state.isLoading}
            rowSelection={{
              ...this.rowSelection,
              selectedRowKeys: this.state.selectedRowKeys,
            }}
          >
            <Table.Column title="ID" dataIndex="id" width={120} />
            <Table.Column title="App" dataIndex="appName" width={120} />
            <Table.Column title="CreateTime" dataIndex="createTime" width={150} />
            <Table.Column title="Description" dataIndex="desc" width={160} />
            <Table.Column
              title="Operate"
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
