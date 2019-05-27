import React, { Component } from 'react';
import { Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { partitionAlgorithms } from '../../../api';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import moment from 'moment';

const { Row, Col } = Grid;

export default class PartitionAlgorithm extends Component {
  static displayName = 'PartitionAlgorithm';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.selectCallback = props.selectCallback;
    this.state = {
      app: props.app,
      obj: {
        dataSource: [],
        isLoading: true,
        pageSize: 4,
        total: 0,
        selected: props.selected,
      },
    };
  }

  componentDidMount() {
    this.objUpdateList();
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.app.id !== nextProps.app.id) {
      this.state = {
        app: nextProps.app,
        obj: {
          allData: [],
          dataSource: [],
          isLoading: true,
          pageSize: 4,
          total: 0,
          selected: nextProps.selected,
        },
      };

      this.objUpdateList();
    } else {
      this.state.obj.selected = nextProps.selected;
    }
    this.setState({});
  }

  objUpdateList = (pageNum) => {

    if(this.state.obj.total == 0 || pageNum == undefined) {
      this.state.obj.isLoading = true;
      this.setState({
        obj: this.state.obj,
      });

      partitionAlgorithms().then((response) => {
        this.state.obj.allData = response.data.data;
        this.state.obj.isLoading = false;
        this.state.obj.total = response.data.data.length;
        this.setState({
          obj: this.state.obj,
        });
        this.objUpdateList(1);
      })
        .catch((error) => {
          console.log(error);
        });
    }else{
      const start = (pageNum-1) * this.state.obj.pageSize;
      const end = start + this.state.obj.pageSize;
      this.state.obj.dataSource = this.state.obj.allData.slice(start, end);
      this.setState({
        obj: this.state.obj,
      });
    }
  }

  objHandleChange = (current) => {
    console.log(current);
    this.objUpdateList(current);
  }

  setObjRowProps = (record, index) => {
    if (this.state.obj.selected != null) {
      if (record.id === this.state.obj.selected.id) {
        return propsConf;
      }
    }
  }

  selectObj = (record, index, e) => {
    console.log(`select : ${record.id} ${this.state.obj.dataSource[index].id}`);
    this.state.obj.selected = record;

    // this.state.form.obj = record.id;
    this.selectCallback(record);

    this.setState({});
  }
  clearObj = () => {
    this.state.obj.selected = null;
    // this.state.form.obj = '';
    this.clearObjCallback();
    this.setState({});
  }

  render() {
    return (
      <div>
        <Table dataSource={this.state.obj.dataSource} loading={this.state.obj.isLoading} onRowClick={this.selectObj} getRowProps={this.setObjRowProps}>
          <Table.Column title="算法名" dataIndex="id" width={40} />
          <Table.Column title="描述" dataIndex="desc" width={160} />
        </Table>
        <div>
          <Pagination pageSize={this.state.obj.pageSize} total={this.state.obj.total} onChange={this.objHandleChange} />
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
