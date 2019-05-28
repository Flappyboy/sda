import { Input, Table, Pagination } from '@alifd/next';
import React, { Component } from 'react';
import {queryInfos, queryNodeInfo} from '../../../api';
import {formatDateForDataList} from "../../../utils/preprocess";

export default class InfoInputTable extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.callback(props.name, arg); };

    this.state = {
      app: this.props.app,
      name: this.props.name,
      type: this.props.type,
      node: null,
      dataSource: [],
      tableDataSource: [],
      loading: true,
      pageSize: 4,
      currentPage: 1,
      total: 0,
      selected: [],
    };
  }

  componentDidMount() {
    let param = {
      appId: this.state.app.id,
      name: this.state.type,
    };
    if(this.state.type == "SYS_NODE"){
      queryNodeInfo(param).then((response) => {
        this.setState({
          node: response.data,
        });
        if(response.data.nodeCount>0){
          const arg = {
            id: this.state.app.id,
            name: this.state.type,
          };
          this.callback( [arg]);
        }
      })
        .catch((error) => {
          console.log(error);
        });
      return;
    }
    queryInfos(param).then((response) => {
      console.log(response.data);
      this.setState({
        dataSource: Object.assign({}, formatDateForDataList(response.data)),
        tableDataSource: response.data.slice((1-1)*this.state.pageSize, 1*this.state.pageSize),
        total: response.data.length,
        loading: false,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  select(record){
    for(let i=0; i<this.state.selected.length; i++){
      if(record.id == this.state.selected[i].id){
        return;
      }
    }
    this.state.selected.push(record);
    this.setState({
      selected: this.state.selected.slice(0),
    });
    this.callback(this.state.selected);
  }

  handlePageChange(current) {
    this.setState({
      currentPage: current,
      tableDataSource: this.state.dataSource.slice((current-1)*this.state.pageSize, current*this.state.pageSize),
    });
  }

  renderOperator = (value, index, record) => {
    return (
      <div>
        <span style={styles.operate} onClick={this.select.bind(this, record)}>
          选择
        </span>
      </div>
    );
  };

  render() {
    if(this.state.type == 'SYS_NODE'){
      if(this.state.node) {
        return (
          <div>
          Node Count: {this.state.node.nodeCount}
        </div>
        );
      }else{
        return (<div></div>);
      }
    }
    return (
      <div>
        <div>
          {this.state.selected.map((info) => {
            return (<span>{info.id}</span>)
          })}
        </div>
        <Table
          dataSource={this.state.tableDataSource}
          loading={this.state.loading}
        >
          <Table.Column title="编码" dataIndex="id" width={80} />
          <Table.Column title="创建日期" dataIndex="createTime" width={80} />
          <Table.Column title="状态" dataIndex="status" width={40} />
          <Table.Column title="描述" dataIndex="desc" width={80} />
          <Table.Column
            title="操作"
            cell={this.renderOperator}
            lock="right"
            width={40}
          />
        </Table>
        <div style={styles.pagination}>
          <Pagination pageSize={this.state.pageSize} current={this.state.currentPage} total={this.state.total} onChange={this.handlePageChange} />
        </div>
      </div>
    );
  }
}

const styles = {
  operate: {
    cursor: 'pointer',
    marginLeft: '10px',
  },
}
