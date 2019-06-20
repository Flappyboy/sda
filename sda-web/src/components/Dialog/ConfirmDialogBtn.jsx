import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';

export default class ConfirmDialogBtn extends Component {
  static displayName = 'ConfirmDialogBtn';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
    };
  }

  onOk = () => {
    this.setState({
      loading: true,
    });
    if(this.props.onOk)
      this.props.onOk(this.onClose.bind(this));
    else
      this.onClose();
  };

  onOpen = () => {
    this.setState({
      visible: true,
    });
  };

  onClose = () => {
    this.setState({
      visible: false,
      loading: false,
    });
    if(this.props.onClose) {
      this.props.onClose();
    }
  };

  render() {
    const okProps = {
      loading: this.state.loading
    };
    return (
      <div style={styles.btn}>
        <span onClick={this.onOpen.bind(this)} type="primary">
          {this.props.children}
        </span>
        <Dialog
          style={{minWidth: 400}}
          title={this.props.title}
          visible={this.state.visible}
          okProps = {okProps}
          onOk={this.onOk.bind(this)}
          onCancel={this.onClose}
          onClose={this.onClose}>
          {this.props.content}
        </Dialog>
      </div>
    );
  }
}

const styles = {
  btn: {
    display: 'inline-block',
  },
};
