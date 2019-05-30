import React, { Component } from 'react';
import { Table, Button, Pagination, Input, Grid, Icon } from '@alifd/next';
import emitter from '../ev';
import {evaluate, evaluationLast, evaluationRedo, queryTaskById} from '../../../../api';
import {AddTaskDialogBtn} from "../../../AddTask";

const { Row, Col } = Grid;

export default class Evaluation extends Component {
  static displayName = 'Evaluation';

  static propTypes = {};

  static defaultProps = {};

  componentWillReceiveProps(nextProps) {
  }

  componentDidMount() {
    // this.eventEmitter = emitter.addListener('query_partition_detail_evaluate', this.evaluate);
    this.queryEvaluate();
  }

  componentWillUnmount() {
    // emitter.removeListener('query_partition_detail_evaluate', this.evaluate);
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
          alert("执行出错！");
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
      this.setState({
        data: response.data,
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

  render() {
    if (this.state.isLoading) {
      return (
        <Icon type="loading" />
      );
    }
    return (
      <div style={styles.info}>
        <AddTaskDialogBtn app={this.state.app} partition={this.state.partition} type="Evaluation" onComplete={this.queryEvaluate.bind(this)}>
          <Button type="primary" size="small">
            <Icon type="add" />添加评估
          </Button>
        </AddTaskDialogBtn>
        {
          this.state.data==null ? null :
            (
              <div>
                <Button onClick={this.redo.bind(this)}>
                  刷新
                </Button>
                {this.state.data.indicators.map((item) => (
                  <div>{item.name}: {item.value}</div>
                ))}
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
  },
};
