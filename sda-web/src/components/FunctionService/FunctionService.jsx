import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import {queryFunctions, queryStatisticsList} from '../../api';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';

const { Row, Col } = Grid;

export default class FunctionService extends Component {
  static displayName = 'FunctionService';

  constructor(props) {
    super(props);
    this.state = {
      type: this.props.type,
      dataSource: [],
      loading: true,
      selected: props.selected,
    };
  }

  componentDidMount() {
    queryFunctions(this.state.type).then((response) => {
      this.setState({
        dataSource: response.data,
        loading: false,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  updateDataSource = (pageNum, queryParam) => {
    // const queryParam = {
    //   pageSize: this.state.pageSize,
    //   page: pageNum,
    // };

  };

  setRowProps = (record, index) => {
    if (this.state.selected != null) {
      if (record.name === this.state.selected.name) {
        return propsConf;
      }
    }
  };

  select = (record, index, e) => {
    // console.log(`select : ${record.name} ${this.state.dataSource[index].name}`);
    this.setState({
      selected: record,
    });
    this.props.select(record);
  };

  render() {
    return (
      <div>
        <Table style={{cursor: 'pointer'}} dataSource={this.state.dataSource} loading={this.state.loading} onRowClick={this.select} getRowProps={this.setRowProps}>
          <Table.Column title="Function Name" dataIndex="name" />
          <Table.Column title="Description" dataIndex="desc" />
        </Table>
        {/*<div>
          <Pagination pageSize={this.state.dynamic.pageSize} total={this.state.dynamic.total} onChange={this.handleChange} />
        </div>*/}
      </div>
    );
  }
}

const preprocess = (dataList) => {
  dataList.forEach(data => {

  });
};

const propsConf = {
  className: 'next-myclass',
  style: { background: 'gray', color: 'white' },
  // onDoubleClick: () => {
  //   console.log('doubleClicked');
  // }
};
