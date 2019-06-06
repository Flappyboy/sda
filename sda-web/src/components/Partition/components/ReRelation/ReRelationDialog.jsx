import React, { Component } from 'react';
import { Dialog, Grid } from '@alifd/next';
import ReRelation from "./ReRelation";
import {reRelation} from "../../../../api";
import {formatDateForDataList} from "../../../../utils/preprocess";

const { Row, Col } = Grid;

export default class ReRelationDialog extends Component {
  static displayName = 'ReRelationDialog';

  constructor(props) {
    super(props);
    this.selected = null;
    this.state = {
      app: props.app,
      reRelation: false,
      partition: props.partition,
    };
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      partition: nextProps.partition,
    });
  }

  componentDidMount() {

  }

  selectCallback = (record) => {
    this.selected = record;
  };

  onOk(){
    console.log(this.selected);
    this.setState({
      reRelation: true,
    });
    const data = {
      partitionInfoId: this.props.partition.id,
      relationInfoIds: this.selected,
    };
    reRelation(data).then((response) => {
      this.setState({
        reRelation: false,
      });
      this.props.onOk();
    })
      .catch((error) => {
        console.log(error);
      });
  }

  onCLose(){
    this.props.onClose();
  }

  render() {
    return (
      <Dialog style={{minWidth: 500, maxWidth: 1000, minHeight: 400}}
              shouldUpdatePosition={true}
              visible={this.props.visible}
              title={this.props.title}
              okProps={{loading: this.state.reRelation}}
              onOk={this.onOk.bind(this)}
              onCancel={this.onCLose.bind(this)}
              onClose={this.onCLose.bind(this)}
      >
        <ReRelation
           app={this.state.app}
           select={this.selectCallback}
           />
      </Dialog>
    );
  }
}

const preprocess = (dataList) => {
  dataList.forEach(data => {

  });
};

const propsConf = {
  className: 'next-myclass',
  style: { background: 'gray', color: 'white' },
  // onDoubleClick: () => {
  //   console.log('doubleClicked');
  // }
};
