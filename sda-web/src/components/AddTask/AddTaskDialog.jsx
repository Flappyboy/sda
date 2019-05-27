import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import AddTask from "./AddTask";

export default class AddTaskDialog extends Component {
  static displayName = 'AddTaskDialog';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      height: window.screen.availHeight*0.8,
      width: window.screen.availWidth*0.7,
    };
  }

  componentDidMount() {

  }


  render() {
    return (
      <Dialog title={`添加信息 ${this.state.app.name}`}
              style={{minHeight: this.state.height, minWidth: this.state.width}}
              visible={this.props.visible}
              footer={false}
              onClose={this.props.onClose}>
        <AddTask app={this.state.app} type={this.state.type} onClose={this.props.onClose}/>
      </Dialog>
    );
  }
}
