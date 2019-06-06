import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import AddTaskDialog from './AddTaskDialog';

const { Row, Col } = Grid;

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;

const formItemLayout = {
  labelCol: { xxs: 8, s: 3, l: 3 },
  wrapperCol: { s: 12, l: 10 }
};
export default class AddTaskConfirmDialog extends Component {
  static displayName = 'AddTaskConfirmDialog';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      visible: true,
      addTask: false,
    };
  }

  componentDidMount() {

  }

  onOk = () => {
    this.setState({
      addTask: true,
      visible: false,
    })
  };

  onClose = () => {
    this.setState({
      addTask: false,
    });
    this.props.onClose();
  };

  render() {
    let addTask = null;
    if(this.state.addTask){
      addTask = (
        <AddTaskDialog visible={true} app={this.state.app} type={this.state.type} onClose={this.onClose} onComplete={this.onClose}/>
      );
    }
    return (
      <div>
        <Dialog
          style={{minWidth: 400}}
          title="Confirm add information"
          visible={this.state.visible}
          onOk={this.onOk.bind(this)}
          onCancel={this.props.onClose}
          onClose={this.props.onClose}>
          {this.props.children}
        </Dialog>
        {addTask}
      </div>
    );
  }
}

const styles = {
  container: {
    paddingBottom: 0,
  },
  subForm: {
    marginBottom: '20px',
  },
  formTitle: {
    margin: '0 0 20px',
    paddingBottom: '10px',
    fontSize: '14px',
    borderBottom: '1px solid #eee',
  },
  formItem: {
    height: '28px',
    lineHeight: '28px',
    marginBottom: '25px',
  },
  formLabel: {
    textAlign: 'right',
  },
  btns: {
    margin: '25px 0',
  },
  resetBtn: {
    marginLeft: '20px',
  },
};
const propsConf = {
  className: 'next-myclass',
  style: { background: 'gray', color: 'white' },
  // onDoubleClick: () => {
  //   console.log('doubleClicked');
  // }
};
