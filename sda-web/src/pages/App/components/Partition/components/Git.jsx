import React, { Component } from 'react';
import { Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';

export default class Git extends Component {
  static displayName = 'Git';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      git: {
        dataSource: [],
        isLoading: true,
        pageSize: 10,
        total: 0,
        selected: {
          id: null,
        },
      },
    };
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      app: nextProps.app,
    });
    this.state.app = nextProps.app;
  }

  gitUpdateList = (pageNum, queryParam) => {
    if (!queryParam) {
      queryParam = {};
    }
    if (this.state.app) {
      queryParam.appId = this.state.app.id;
    }
    queryParam.pageSize = this.state.git.pageSize;
    queryParam.page = pageNum;

    this.state.git.isLoading = true;
    this.setState({
      git: this.state.git,
    });
    queryGitList(queryParam).then((response) => {
      console.log(response.data.data);
      this.preprocess(response.data.data.list);
      this.state.git.dataSource = response.data.data.list;
      this.state.git.isLoading = false;
      this.state.git.total = response.data.data.total;
      this.setState({
        git: this.state.git,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  gitHandleChange = (current) => {
    console.log(current);
    this.gitUpdateList(current);
  }

  setGitRowProps = (record, index) => {
    if (this.state.git.selected != null) {
      if (record.id === this.state.git.selected.id) {
        return propsConf;
      }
    }
  }

  selectGit = (record, index, e) => {
    console.log(`select : ${record.id} ${this.state.git.dataSource[index].id}`);
    this.state.git.selected = record;
    this.state.form.git = record.id;
    this.setState({});
  }

  render() {
    return (
      <Table dataSource={this.state.git.dataSource} loading={this.state.git.isLoading} onRowClick={this.selectGit} getRowProps={this.setGitRowProps}>
        <Table.Column title="编码" dataIndex="id" width={120} />
        <Table.Column title="逻辑耦合" dataIndex="logicCouplingFactor" width={150} />
        <Table.Column title="修改频率" dataIndex="modifyFrequencyFactor" width={150} />
        <Table.Column title="可靠性" dataIndex="reliabilityFactor" width={160} />
        <Table.Column title="描述" dataIndex="desc" width={160} />
      </Table>
      <div style={styles.pagination}>
        <Pagination pageSize={this.state.git.pageSize} total={this.state.git.total} onChange={this.gitHandleChange} />
      </div>
    );
  }

}
