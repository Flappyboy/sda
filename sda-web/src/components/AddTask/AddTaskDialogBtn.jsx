import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import AddTaskDialog from "./AddTaskDialog";

export default class AddTaskDialogBtn extends Component {
  static displayName = 'AddTaskDialogBtn';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      visible: props.visible || false,
    };
  }

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

  onComplete() {
    if(this.props.onComplete){
      this.props.onComplete();
    }
    this.onClose();
  }

  render() {
    const okProps = {
      loading: this.state.loading
    };
    return (
      <span style={styles.editDialog}>
        <span onClick={this.onOpen.bind(this)}>
          {this.props.children}
        </span>
        <AddTaskDialog visible={this.state.visible} partition={this.props.partition} app={this.state.app} type={this.state.type} onClose={this.onClose.bind(this)} onComplete={this.onComplete.bind(this)}/>
      </span>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
  },
};
