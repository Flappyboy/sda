import React, { Component } from 'react';
import {updatePartition} from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import PartitionDialog from "./PartitionDialog";

export default class EditPartitionDialogBtn extends Component {
  static displayName = 'EditDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      obj: props.obj,
      loading: false,
    };
  }

  handleSubmit = (values) => {
      values.id = this.state.obj.id;
      this.setState({
        loading: true,
      });
      updatePartition(values)
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
          {this.props.children}
        </span>
        <PartitionDialog okProps={okProps}
                         visible={this.state.visible}
                         onClose={this.onClose.bind(this)}
                         onCancel={this.onClose.bind(this)}
                         onOk={this.handleSubmit}
                         obj={this.state.obj}
                         title="Edit Partition"/>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
  },
};
