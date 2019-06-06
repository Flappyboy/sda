import {queryPairRelationsByAppId, reRelation} from '../../../../api';
import { Input, Table, Pagination, Grid, Card, Tag, Button, Field  } from '@alifd/next';
import React, { Component } from 'react';
import {formatDateForDataList} from "../../../../utils/preprocess";

const { Row, Col} = Grid;
const commonProps = {
  style: { width: 300 },
};
const {Group: TagGroup, Closeable: CloseableTag} = Tag;

const handler = from => {
  console.log(`close from ${from}`);
  return false;
};

export default class InfoInputTable extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.select(arg) };

    this.state = {
      app: this.props.app,
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
  }

  getValues = () => {
    const values = this.field.getValues();
    console.log(values);
  };

  /*componentWillReceiveProps(nextProps) {
    if(nextProps.meta != this.state.meta){
      this.state.app = nextProps.app;
      this.state.name = nextProps.name;
      this.state.type = nextProps.type;
      this.setState({
        app: nextProps.app,
        name: nextProps.name,
        node: null,
        dataSource: [],
        tableDataSource: [],
        loading: true,
        pageSize: 4,
        currentPage: 1,
        total: 0,
        selected: [],
        tableSource: [],
      });
      this.componentDidMount();
    }
  }*/


  componentDidMount() {
    queryPairRelationsByAppId(this.state.app.id).then((response) => {
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
    this.state.selected.push(record.id);
    this.setState({
      selected: this.state.selected.slice(0),
    });
    this.callback(this.state.selected);
  }

  deleteSelect(id){
    for(let i=0; i<this.state.selected.length; i++){
      if(id == this.state.selected[i].id){
        this.state.selected.splice(i,1);
        break;
      }
    }
    this.setState({
      selected: this.state.selected.slice(0),
    });
    this.callback(this.state.selected);
    return false;
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
      <div style={{marginTop: 20}}>
        <div>
          <TagGroup style={{marginTop:"20px"}}>
            {this.state.selected.map((infoId) => {
              return (
                <CloseableTag marginTop="20px" marginBottom="10px" onClose={this.deleteSelect.bind(this, infoId)}>
                  {infoId}
                </CloseableTag>
              )
            })}
          </TagGroup>
        </div>
        <div style={{marginTop:"20px"}}>
          <Table
            dataSource={this.state.tableDataSource}
            loading={this.state.loading}
          >
            <Table.Column title="ID" dataIndex="id" width={80} />
            <Table.Column title="Info Name" dataIndex="name" width={80} />
            <Table.Column title="CreateTime" dataIndex="createTime" width={80} />
            <Table.Column title="Status" dataIndex="status" width={40} />
            <Table.Column title="Description" dataIndex="desc" width={80} />
            <Table.Column
              title="Operate"
              cell={this.renderOperator}
              lock="right"
              width={40}
            />
          </Table>
          <div style={styles.pagination}>
            <Pagination pageSize={this.state.pageSize} current={this.state.currentPage} total={this.state.total} onChange={this.handlePageChange} />
          </div>
        </div>
        {/*<div style={styles.paginatio}>
            <Row justify="center" style={{marginTop:"20px", marginBottom:"10px"}}>
              <Col span="6" style={{ height: '50px', lineHeight: '50px' }}>

              </Col>
            </Row>
          </div>*/}
      </div>
    );
  }
}

const styles = {
  pagination: {
    textAlign: 'right',
    paddingTop: '18px',
  },
  operate: {
    cursor: 'pointer',
    marginLeft: '10px',
  },
}
