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

  render() {
    const okProps = {
      loading: this.state.loading
    };
    return (
      <div style={styles.editDialog}>
        <span onClick={this.onOpen.bind(this)}>
          {this.props.children}
        </span>
        <AddTaskDialog visible={this.state.visible} app={this.state.app} type={this.state.type} onClose={this.onClose} onComplete={this.onClose}/>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
  },
};
