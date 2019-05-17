import React, { Component } from 'react';
import { Table, Pagination, Input, Grid, Icon } from '@alifd/next';
import emitter from '../ev';
import { evaluate } from '../../../../api';

const { Row, Col } = Grid;

export default class Evaluation extends Component {
  static displayName = 'Evaluation';

  static propTypes = {};

  static defaultProps = {};

  componentWillReceiveProps(nextProps) {
    // this.state.partition = nextProps.partition;
    // this.setState({
    //   partition: nextProps.partition,
    // });
  }

  componentDidMount() {
    this.eventEmitter = emitter.addListener('query_partition_detail_evaluate', this.evaluate);
  }

  componentWillUnmount() {
    emitter.removeListener('query_partition_detail_evaluate', this.evaluate);
  }

  constructor(props) {
    super(props);
    this.state = {
      init: true,
      dataSource: [],
      isLoading: true,
    };
  }

  render() {
    if (this.state.init) {
      return (
        <div />
      );
    }
    return (
      <div style={styles.info}>
        {this.state.dataSource.map((item) => (
          <div>{item.name}: {item.value}</div>
        ))}
      </div>
    );
  }

  evaluate = (partition) => {

    const queryParam = {
      id: partition.id,
      evaluationPluginName: 'SYS_Demo',
    };
    this.setState({
      init: false,
      isLoading: true,
    });
    evaluate(queryParam).then((response) => {
      this.setState({
        dataSource: response.data.data.indicators,
        isLoading: false,
      });
    })
      .catch((error) => {
        console.log(error);
        this.setState({
          isLoading: false,
        });
      });
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
