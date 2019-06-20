import React, { Component } from 'react';
import { Table, Button, Pagination, Input, Grid, Icon } from '@alifd/next';
import emitter from '../ev';
import {evaluate, evaluationLast, evaluationRedo, queryTaskById} from '../../../../api';
import {AddTaskDialogBtn} from "../../../AddTask";
import ConfirmDialogBtn from "../../../Dialog/ConfirmDialogBtn";

const { Row, Col } = Grid;

export default class Evaluation extends Component {
  static displayName = 'Evaluation';

  static propTypes = {};

  static defaultProps = {};

  componentWillReceiveProps(nextProps) {
  }

  componentDidMount() {
    this.eventEmitter = emitter.addListener('query_partition_detail_evaluate', this.queryEvaluate);
    this.queryEvaluate();
  }

  componentWillUnmount() {
    emitter.removeListener('query_partition_detail_evaluate', this.queryEvaluate);
    if(this.interval != null){
      clearInterval(this.interval);
    }
  }

  constructor(props) {
    super(props);
    this.state = {
      partition: props.partition,
      data: null,
      isLoading: true,
      app: props.app,
      indicatorTable: null,
    };
  }

  flag = false;

  check(taskId) {
    if(this.flag){
      return;
    }
    this.flag = true;
    queryTaskById(taskId).then((response) => {
      if(response.data && response.data.status){
        if(response.data.status == 'Doing'){
          return;
        }
        if(response.data.status == 'Error'){
          alert("Error！");
          clearInterval(this.interval);
          this.interval = null;
        }
        if(response.data.status != 'Doing'){
          clearInterval(this.interval);
          this.interval = null;
          this.queryEvaluate();
        }
      }
    })
      .catch((error) => {
        console.log(error);
      }).finally(() => {
        this.flag = false;
    });
  }

  redo() {
    this.setState({
      isLoading: true,
    });
    evaluationRedo(this.state.data.id).then((response) => {
      this.interval = setInterval(this.check.bind(this, response.data),1000);
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });
  }

  queryEvaluate = () => {
    this.setState({
      isLoading: true,
    });
    evaluationLast(this.state.partition.id).then((response) => {

      if(response.data && response.data.indicators){
        const table = {};
        response.data.indicators.map((item) => (
          table[item.name] = item.value
        ));
        this.setState({
          indicatorTable: [table],
        })
      }
      this.setState({
        data: response.data? response.data : null,
        isLoading: false,
      });
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });
  };

  titles(item) {
    let desc = null;
    switch (item.name) {
      case "Instability":
        desc = (<span>Instability represents the ratio of efferent coupling to total coupling. Instability is less, the better.</span>);
        break;
      case "CHM":
        desc = (<span>CHM is used to measure the average cohesion of service interfaces at message level.CHM is more, the better.</span>);
        break;
      case "CHD":
        desc = (<span>CHD is used to measure the average cohesion of service interfaces at domain level.CHD is more, the better.</span>);
        break;
      case "IFN":
        desc = (<span>IFN indicates the number of interfaces provided by an extracted service to other services averagely. IFN is smaller, the better.</span>);
        break;
      case "OPN":
        desc = (<span>OPN denotes the number of operations provided by the extracted microservices. OPN is less, the better.</span>);
        break;
      case "IRN":
        desc = (<span>IRN represents the number of method calls across two services. IRN is smaller, the better.</span>);
        break;
    }
    // CHD (CoHesion at Domain level), CHM (CoHesion at Message level), IFN (InterFace Number), OPN (OPeration Number), IRN (InteRaction Number)

    let desc2 = null;
    const desc2style={
      marginLeft: 5
    };
    switch (item.name) {
      case "Instability":
        desc2 = (<span style={desc2style}>(CoHesion at Domain level)</span>);
        break;
      case "CHM":
        desc2 = (<span style={desc2style}>(CoHesion at Message level)</span>);
        break;
      case "CHD":
        desc2 = (<span style={desc2style}>(CoHesion at Domain level)</span>);
        break;
      case "IFN":
        desc2 = (<span style={desc2style}>(InterFace Number)</span>);
        break;
      case "OPN":
        desc2 = (<span style={desc2style}>(OPeration Number)</span>);
        break;
      case "IRN":
        desc2 = (<span style={desc2style}>(InteRaction Number)</span>);
        break;
    }
    return (
      <span>
        {item.name}
        {desc2}
        <ConfirmDialogBtn title="Description" content={desc}>
          <Icon style={{color: '#373737', marginLeft: 6, cursor: "pointer"}} size="small" type="help"/>
        </ConfirmDialogBtn>
      </span>
    );
  }

  render() {
    if (this.state.isLoading) {
      return (
        <Icon type="loading" style={{minHeight: 150}} />
      );
    }
    return (
      <div style={styles.info}>
        <AddTaskDialogBtn app={this.state.app} partition={this.state.partition} type="Evaluation" onComplete={this.queryEvaluate.bind(this)}>
          <Button type="primary" size="small">
            <Icon type="add" />Add Evaluation
          </Button>
        </AddTaskDialogBtn>
        {
          this.state.data==null ? null :
            (
              <span>
                <Button style={{marginLeft: 10}} onClick={this.redo.bind(this)}  size="small">
                  Refresh
                </Button>
              </span>
            )
        }
        {
          this.state.indicatorTable==null ? null :
            (
              <div style={{marginTop: 10}}>
                {/*{this.state.data.indicators.map((item) => (
                  <div>{item.name}: {item.value}</div>
                ))}*/}
                <Table dataSource={this.state.indicatorTable} hasBorder={false} fixedHeader stickyHeader={false}>
                  {this.state.data.indicators.map((item) => (
                  <Table.Column title={this.titles.bind(this, item)} dataIndex={item.name} />
                  ))}
                </Table>
              </div>
            )
        }
      </div>
    );
  }
}
const styles = {
  pagination: {
    textAlign: 'right',
    paddingTop: '26px',
  },
  info: {
    margin: 20,
    minHeight: 140,
  },
};
