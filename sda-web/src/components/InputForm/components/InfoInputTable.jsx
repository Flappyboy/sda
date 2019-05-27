import { Input, Table, Pagination } from '@alifd/next';
import React, { Component } from 'react';
import {queryInfos} from '../../../api';

export default class InfoInputTable extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.callback(props.name, arg); };

    this.state = {
      app: this.props.app,
      name: this.props.name,
      type: this.props.type,
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
    queryInfos(param).then((response) => {
      console.log(response.data);
      this.setState({
        dataSource: response.data[this.state.type],
        tableDataSource: this.state.dataSource.slice((1-1)*this.state.pageSize, 1*this.state.pageSize),
        total: response.data.length,
        loading: false,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  select(record){
    const select = this.state.selected.push(record);
    this.setState({
      selected: Object.assign({}, select),
    });
    this.callback(select);
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
          <Table.Column title="创建日期" dataIndex="createTime" width={140} />
          <Table.Column title="状态" dataIndex="status" width={140} />
          <Table.Column title="描述" dataIndex="desc" width={140} />
          <Table.Column
            title="操作"
            cell={this.renderOperator}
            lock="right"
            width={200}
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
