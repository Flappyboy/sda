import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import {queryTaskById} from "../../api";

export default class AddTaskDialog extends Component {
  static displayName = 'AddTaskDialog';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      task: props.task,
      status: 'Doing',
    };
    this.interval = null;
  }

  componentDidMount() {
    this.interval = setInterval(this.check.bind(this),1000);
  }

  componentWillUnmount() {
    if(this.interval != null){
      clearInterval(this.interval);
    }
  }

  check() {
    queryTaskById(this.state.task.id).then((response) => {
      if(response.data && response.data.status){
        this.setState({
          status: response.data.status,
        });
        if(response.data.status != 'Doing'){
          clearInterval(this.interval);
          this.interval = null;
        }
      }
    })
      .catch((error) => {
        console.log(error);
      });
  }

  onClose(){
    this.props.onClose();
  }
  onComplete(){
    if(this.props.onComplete){
      this.props.onComplete();
    }else{
      this.onClose();
    }
  }

  render() {
    let content = null;
    let footer = null;
    switch (this.state.status) {
      case 'Doing':
        content = "任务已创建完成，正在执行中。。。。。。";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>后台执行</Button>);
        break;
      case 'Complete':
        content = "任务已执行完成！";
        footer = (<Button type="primary" onClick={this.onComplete.bind(this)}>Ok</Button>);
        break;
      case 'Warn':
        content = "任务已执行完成, 但存在警告！";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>Ok</Button>);
        break;
      case 'Error':
        content = "任务执行出错，已终止！";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>Ok</Button>);
        break;
      default:
        alert("Wrong status: "+this.state.status);
        break;
    }
    return (
      <Dialog title="任务"
              style={{minHeight: 300, minWidth: 400}}
              visible={true}
              footer={footer}
              onClose={this.onClose.bind(this)}>
        {content}
      </Dialog>
    );
  }
}
