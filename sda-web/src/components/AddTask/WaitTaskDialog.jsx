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

  flag = false;

  componentDidMount() {
    this.interval = setInterval(this.check.bind(this),1000);
  }

  componentWillUnmount() {
    if(this.interval != null){
      clearInterval(this.interval);
    }
  }

  check() {
    if(this.flag){
      return;
    }
    this.flag = true;
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
      }).finally(()=>{
        this.flag = false;
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
        content = "The task has been created and is being executed ......";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>Background Execute</Button>);
        break;
      case 'Complete':
        content = "Task accomplished!";
        footer = (<Button type="primary" onClick={this.onComplete.bind(this)}>Ok</Button>);
        break;
      case 'Warn':
        content = "The task has been completed, but there is a warning!";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>Ok</Button>);
        break;
      case 'Error':
        content = "Error execution of task terminated!";
        footer = (<Button type="primary" onClick={this.onClose.bind(this)}>Ok</Button>);
        break;
      default:
        alert("Wrong status: "+this.state.status);
        break;
    }
    return (
      <Dialog title="Task"
              style={{minHeight: 150, minWidth: 400}}
              visible={true}
              footer={footer}
              onClose={this.onClose.bind(this)}>
        {content}
      </Dialog>
    );
  }
}
