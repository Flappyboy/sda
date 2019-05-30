import { Input, Table, Pagination, Grid, Card, Tag, Button, Field  } from '@alifd/next';
import React, { Component } from 'react';
import {queryInfos, queryNodeInfo} from '../../../api';
import {formatDateForDataList} from "../../../utils/preprocess";

const { Row, Col} = Grid;
const commonProps = {
  style: { width: 300 },
  title: 'Related Info'
};
const {Group: TagGroup, Closeable: CloseableTag} = Tag;

const handler = from => {
  console.log(`close from ${from}`);
  return false;
};

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
      tableSource: [],
    };
    this.idx = 0;

    //dynamic table
    this.field = new Field(this, {
      parseName: true,
    });
  }

  getValues = () => {
    const values = this.field.getValues();
    console.log(values);
  }

  add = () => {
    const { tableSource } = this.state;
    tableSource.push({
      // id: ++this.idx
      id : this.state.selected.map((info) => {
        return (<span>{info.id}</span>)
      })
    });

    this.setState({ tableSource });
  }

  removeItem(index) {
    const { tableSource } = this.state;
    tableSource.splice(index, 1);
    this.field.spliceArray('name.{index}', index);
    this.setState({ tableSource });
  }

  delete = (value, index) => <Button warning onClick={this.removeItem.bind(this, index)}>delete</Button>;


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

  renderTagList (option) {
    return [
      <CloseableTag type={option.type}>
        {this.state.selected.map((info) => {
          return (<span>{info.id}</span>)
        })}
      </CloseableTag>
    ];
  }

  render() {
    if(this.state.type == 'SYS_NODE'){
      if(this.state.node) {
        return (
        <div>
          <Card {...commonProps} contentHeight="auto" style={{marginTop:'10px', marginBottom: '10px', width:'288px' }}>
            <div className="custom-content">
              <p>
                Node Count: {this.state.node.nodeCount}
              </p>
            </div>
          </Card>
        </div>
        );
      }else{
        return (
          <div>
          </div>
        );
      }
    }
    return (
      <div>
        {/*<TagGroup>*/}
          {/*/!*{this.renderTagList({type: 'normal'})}*!/*/}
          {/*<CloseableTag onClose={() => true}>*/}
            {/*{this.state.selected.map((info) => {*/}
              {/*return (<span>{info.id}</span>)*/}
            {/*})}*/}
          {/*</CloseableTag>*/}
        {/*</TagGroup>*/}
        <div marginTop="10px">
          <Table  dataSource={this.state.tableSource}>
            <Table.Column title="已选编码" dataIndex="id" />
            <Table.Column title="操作" cell={this.delete} width={100} />
          </Table>
        </div>
        <div marginTop="20px">
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
              onClick={this.add}
            />
          </Table>
        </div>
        <div style={styles.pagination}>
          <Row justify="center">
            <Col span="6" style={{ height: '50px', lineHeight: '50px' }}>
              <Pagination pageSize={this.state.pageSize} current={this.state.currentPage} total={this.state.total} onChange={this.handlePageChange} />
            </Col>
          </Row>
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
