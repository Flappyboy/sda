import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Grid, Button } from '@alifd/next';
import ServiceContent from './ServiceContent';
import Graph from './Graph';
import Evaluation from './Evaluation';
import emitter from '../ev';
import { queryPartitionDetail } from '../../../../api';
import {ReRelationBtn} from "../ReRelation";

const { Row, Col } = Grid;

export default class PartitionDetail extends Component {
  static displayName = 'PartitionDetail1';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      partition: props.partition,
      show: 0,
      isLoading: true,
    };
  }

  componentDidMount() {
    this.eventEmitter = emitter.addListener('query_partition_detail', this.queryPartitionDetails);
  }
  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('query_partition_detail', this.queryPartitionDetails);
  }

  queryPartitionDetails = (partition, flag) => {
    if(partition == null || partition == undefined){
      partition = this.state.partition;
    }
    this.setState({
      partition: partition,
      show: flag ? 2 : 1,
      isLoading: true,
    });
    const id = partition.id;
    console.log(id);
    this.jump();
    queryPartitionDetail(id).then((response) => {
      console.log(response.data.data);
      response.data.data.id = id;
      this.setState({
        data: response.data.data,
        show: 2,
        isLoading: false,
      });
      // emitter.emit("query_partition_detail_evaluate",response.data.data);
      this.jump();
    })
      .catch((error) => {
        console.log(error);
      });
  };

  jump(){
    // console.log(document.getElementById('partition-detail').getBoundingClientRect());
    if(document.getElementById('partition-detail').getBoundingClientRect().top <= 250){
      return;
    }
    // 找到锚点
    const anchorElement = document.getElementById('partition-detail');
    // 如果对应id的锚点存在，就跳转到锚点
    if (anchorElement) { anchorElement.scrollIntoView(); }
  }

  render() {
    if (this.state.show === 0) {
      return (<div id="partition-detail" />);
    }

    let service = null;
    let evaluation = null;
    if (this.state.show === 2 && this.state.partition != null) {
      service = (
        <ServiceContent partition={this.state.partition}
                        partitionDetail={this.state.data}
                        isLoading={this.state.isLoading} />
      );
      evaluation = (
        <Evaluation app={this.state.app} partition={this.state.partition} partitionDetail={this.state.data}/>
      );
    }
    return (
      <IceContainer style={styles.container}>
        <h4 id="partition-detail" style={styles.title}>{this.state.partition.id} {this.state.partition.desc}</h4>
        <div style={{marginLeft: 15}}>
          <Row wrap>
            <Col l="12">
              <ReRelationBtn>
                <Button type="primary">
                  重新生成依赖关系
                </Button>
              </ReRelationBtn>
              <Graph isLoading={this.state.isLoading}
                     partition={this.state.partition}
                     data={this.state.data}/>
            </Col>
            <Col l="12">
              {service}
            </Col>
          </Row>
          <Row>
            <Col l="12">
              {evaluation}
            </Col>
          </Row>
         </div>
      </IceContainer>
    );
  }
}

const styles = {
  container: {
    padding: '0',
  },
  title: {
    margin: '0 0 20px',
    padding: '15px',
    fonSize: '16px',
    color: 'rgba(0,0,0,0.89)',
    fontWeight: '500',
    borderBottom: '1px solid',
    borderColor: '#f0f0f0',
  },
};
