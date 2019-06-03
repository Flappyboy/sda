import React, { Component } from 'react';
import { Dialog, Grid } from '@alifd/next';
import ReRelation from "./ReRelation";

const { Row, Col } = Grid;

export default class ReRelationDialog extends Component {
  static displayName = 'ReRelationDialog';

  constructor(props) {
    super(props);
    this.selected = null;
    this.state = {
      app: this.props.app,
    };
  }

  componentDidMount() {
  }

  selectCallback = (record) => {
    this.selected = record;
  };

  onOk(){
    this.props.onOk(this.selected);
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
