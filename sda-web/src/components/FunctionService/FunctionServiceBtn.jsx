import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import FunctionServiceDialog from "./FunctionServiceDialog";

export default class FunctionServiceBtn extends Component {
  static displayName = 'FunctionServiceBtn';

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
    if(this.props.clickCallback){
      this.props.clickCallback();
    }
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
        <FunctionServiceDialog
                   visible={this.state.visible}
                   onClose={this.onClose.bind(this)}
                   onCancel={this.onClose.bind(this)}
                   onOk={this.onOk.bind(this)}
                   app={this.state.app}
                   type={this.state.type}
                   title="Select Function"/>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
  },
};
