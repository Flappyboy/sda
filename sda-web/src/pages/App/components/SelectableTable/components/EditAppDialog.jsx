import React, { Component } from 'react';
import {updateApp} from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import AppDialog from "./AppDialog";

export default class EditAppDialog extends Component {
  static displayName = 'EditDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      app: props.app,
      loading: false,
    };
  }

  handleSubmit = (values) => {
      values.id = this.state.app.id;
      this.setState({
        loading: true,
      });
      updateApp(values)
        .then((response) => {
        this.props.editCallback(response.data);
      }).catch((error) => {
        console.log(error);
      }).finally(() => {
        this.setState({
          visible: false,
          loading: false,
        });
      });
  };

  onClose = () => {
    this.setState({
      visible: false,
      loading: false,
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
    const okProps = {
      loading: this.state.loading
    };
    return (
      <div style={styles.editDialog}>
        <span onClick={this.onOpen.bind(this)}>
          Edit
        </span>
        <AppDialog okProps={okProps}
                   visible={this.state.visible}
                   onClose={this.onClose.bind(this)}
                   onCancel={this.onClose.bind(this)}
                   onOk={this.handleSubmit}
                   app={this.state.app}
                   title="Edit App"/>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
  },
};
