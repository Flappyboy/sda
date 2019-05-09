import React, { Component } from 'react';
import { Table, Pagination } from '@alifd/next';
import IceContainer from '@icedesign/container';
import { queryCallList } from '../../../../api';
import emitter from '../ev';



export default class DetailTable extends Component {
  static displayName = 'DetailTable';

  static propTypes = {};

  static defaultProps = {};

  updateList = (pageNum) => {
    const queryParam = {
      dynamicAnalysisInfoId: this.state.dynamicAnalysisInfoId,
      type: 0,
      pageSize: this.state.pageSize,
      page: pageNum,
    };
    this.setState({
      isLoading: true,
    });
    queryCallList(queryParam).then((response) => {
      console.log(response.data.data);

      this.setState({
        dataSource: response.data.data.list,
        isLoading: false,
        total: response.data.data.total,
      });
      // 找到锚点
      const anchorElement = document.getElementById('statistics-detail');
      // 如果对应id的锚点存在，就跳转到锚点
      if (anchorElement) { anchorElement.scrollIntoView(); }
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
    this.eventEmitter = emitter.addListener('query_statistics_detail', this.queryStatisticsDetail);
    this.eventEmitter = emitter.addListener('clear_statistics_detail', this.clearStatisticsDetail);
    // 找到锚点
    const anchorElement = document.getElementById('statistics-detail');
    // 如果对应id的锚点存在，就跳转到锚点
    if (anchorElement) { anchorElement.scrollIntoView(); }
  }

  componentWillUnmount() {
    emitter.removeListener('query_statistics_detail', this.queryStatisticsDetail);
    emitter.removeListener('clear_statistics_detail', this.clearStatisticsDetail);
  }

  constructor(props) {
    super(props);

    this.state = {
      dataSource: [],
      show: false,
      pageSize: 10,
      total: 0,
    };
  }

  queryStatisticsDetail = (param) => {
    this.state.dynamicAnalysisInfoId = param;
    this.setState({
      dynamicAnalysisInfoId: param,
      show: true,
      isLoading: true,
    });
    this.updateList(1);
    console.log('query aha ', param);
  };

  clearStatisticsDetail = () => {
    this.setState({
      show: false,
    });
  }

  render() {
    if (!this.state.show) {
      return (<div id="statistics-detail" />);
    }
    return (
      <div id="statistics-detail" className="selectable-table" style={styles.selectableTable}>
        <IceContainer>
          <Table
            dataSource={this.state.dataSource}
            isLoading={this.state.isLoading}
          >
            <Table.Column title="调用类" dataIndex="caller" width={120} />
            <Table.Column title="被调用类" dataIndex="callee" width={120} />
            <Table.Column title="次数" dataIndex="count" width={150} />
          </Table>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} total={this.state.total} onChange={this.handleChange} />
          </div>
        </IceContainer>
      </div>
    );
  }
}
const styles = {
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
