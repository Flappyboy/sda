import React, { Component } from 'react';
import { addApp } from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import '../../../../../api';
import AppDialog from "./AppDialog";
import {AddTaskConfirmDialog} from "../../../../../components/AddTask";

export default class AddAppDialog extends Component {
  static displayName = 'EditDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      loading: false,
      newApp: null,
    };
  }

  handleSubmit = (values) => {
    this.setState({
      visible: true,
    });
    addApp(values).then((response) => {
      this.props.addNewItem(response.data);
      this.setState({
        visible: false,
        newApp: response.data,
      });
    })
      .catch((error) => {
        console.log(error);
      }).finally(() => {
      this.setState({
        loading: false,
      });
    });
  };

  onOpen = () => {
    this.setState({
      visible: true,
    });
  };

  onClose = () => {
    this.setState({
      visible: false,
    });
  };

  clearNewApp = () => {
    this.setState({
      newApp: null,
    })
  }

  render() {
    const okProps = {
      loading: this.state.loading
    };
    let addwork = null;
    if(this.state.newApp){
      addwork = (
        <AddTaskConfirmDialog app={this.state.newApp} type="InfoCollection" onClose={this.clearNewApp}>
          应用已添加成功，
          是否向应用中添加信息？
        </AddTaskConfirmDialog>
        )
    }
    return (
      <div style={styles.editDialog}>
        <Button
          size="small"
          type="primary"
          onClick={() => this.onOpen()}
        >
          增加
        </Button>
        <AppDialog okProps={okProps}
                   visible={this.state.visible}
                   onClose={this.onClose.bind(this)}
                   onCancel={this.onClose.bind(this)}
                   onOk={this.handleSubmit}
                   title="添加应用"/>
        {addwork}
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
    marginRight: '5px',
  },
};
