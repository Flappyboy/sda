import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import ReRelationDialog from "./ReRelationDialog";

export default class ReRelationBtn extends Component {
  static displayName = 'ReRelationBtn';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      visible: props.visible!=null ? props.visible : false,
    };
  }

  onOk = (selected) => {
    if(selected != null && selected != undefined)
      this.props.select(selected);
    this.onClose();
  };

  onClose = () => {
    this.setState({
      visible: false,
    });
  };

  onOpen = () => {
    this.setState({
      visible: true,
    });
  };

  callback = (filepath) => {
    this.setState({
      filepath,
    });
  };

  render() {
    return (
      <div style={styles.editDialog}>
        <span onClick={this.onOpen.bind(this)}>
          {this.props.children}
        </span>
        <ReRelationDialog
                   visible={this.state.visible}
                   onClose={this.onClose.bind(this)}
                   onCancel={this.onClose.bind(this)}
                   onOk={this.onOk.bind(this)}
                   app={this.state.app}
                   title="选择功能"/>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    // display: 'inline-block',
  },
};
