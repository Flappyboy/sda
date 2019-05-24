import React, { Component } from 'react';
import { Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { queryStatisticsList, queryStatistics, delStatistics, addStatistics, addPartition, queryGitList } from '../../../../../api';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import moment from 'moment';

const { Row, Col } = Grid;

import emitter from '../../ev';


export default class DynamicData extends Component {
  static displayName = 'DynamicData';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.selectCallback = props.selectCallback;
    this.state = {
      app: props.app,
      dynamic: {
        dataSource: [],
        isLoading: true,
        pageSize: 10,
        total: 0,
        selected: props.selected,
      },
    };
  }

  componentDidMount() {
    this.dynamicUpdateList(1);
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.app.id !== nextProps.app.id) {
      this.state = {
        app: nextProps.app,
        dynamic: {
          dataSource: [],
          isLoading: true,
          pageSize: 10,
          total: 0,
          selected: nextProps.selected,
        },
      };

      this.dynamicUpdateList(1);
    } else {
      this.state.dynamic.selected = nextProps.selected;
    }
    this.setState({});
  }

  dynamicUpdateList = (pageNum, queryParam) => {
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
    queryParam.pageSize = this.state.dynamic.pageSize;
    queryParam.page = pageNum;

    this.state.dynamic.isLoading = true;
    this.setState({
      dynamic: this.state.dynamic,
    });
    queryStatisticsList(queryParam).then((response) => {
      console.log(response.data.data);
      preprocess(response.data.data.list);
      this.state.dynamic.dataSource = response.data.data.list;
      this.state.dynamic.isLoading = false;
      this.state.dynamic.total = response.data.data.total;
      this.setState({
        dynamic: this.state.dynamic,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  dynamicHandleChange = (current) => {
    console.log(current);
    this.dynamicUpdateList(current);
  }

  setDynamicRowProps = (record, index) => {
    if (this.state.dynamic.selected != null) {
      if (record.id === this.state.dynamic.selected.id) {
        return propsConf;
      }
    }
  }

  selectDynamic = (record, index, e) => {
    console.log(`select : ${record.id} ${this.state.dynamic.dataSource[index].id}`);
    this.state.dynamic.selected = record;

    // this.state.form.dynamic = record.id;
    this.selectCallback(record);

    this.setState({});
  }
  clearDynamic = () => {
    this.state.dynamic.selected = null;
    // this.state.form.dynamic = '';
    this.clearDynamicCallback();
    this.setState({});
  }

  render() {
    return (
      <div>
        <Table dataSource={this.state.dynamic.dataSource} loading={this.state.dynamic.isLoading} onRowClick={this.selectDynamic} getRowProps={this.setDynamicRowProps}>
          <Table.Column title="编码" dataIndex="id" width={120} />
          <Table.Column title="创建日期" dataIndex="createTime" width={150} />
          <Table.Column title="开始日期" dataIndex="startTime" width={150} />
          <Table.Column title="结束日期" dataIndex="endTime" width={160} />
          <Table.Column title="描述" dataIndex="desc" width={160} />
        </Table>
        <div>
          <Pagination pageSize={this.state.dynamic.pageSize} total={this.state.dynamic.total} onChange={this.dynamicHandleChange} />
        </div>
      </div>
    );
  }
}

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

const preprocess = (dataList) => {
  dataList.forEach(data => {
    data.createTime = moment(data.createdat).format(DATE_FORMAT);
    if (data.starttine) {
      data.startTime = moment(data.starttine).format(DATE_FORMAT);
    }
    if (data.endtime) {
      data.endTime = moment(data.endtime).format(DATE_FORMAT);
    }
    if (!('status' in data)) {
      data.status = true;
    }
  });
}

const propsConf = {
  className: 'next-myclass',
  style: { background: 'gray', color: 'white' },
  // onDoubleClick: () => {
  //   console.log('doubleClicked');
  // }
};