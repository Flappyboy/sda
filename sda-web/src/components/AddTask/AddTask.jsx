import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { queryStatisticsList, queryStatistics, delStatistics, addStatistics, addPartition, queryGitList } from '../../api';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import FunctionServiceBtn from "../FunctionService/FunctionServiceBtn";
import InputForm from "../InputForm";

const { Row, Col } = Grid;

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;

const formItemLayout = {
  labelCol: { xxs: 8, s: 3, l: 3 },
  wrapperCol: { s: 12, l: 10 }
};
export default class AddTask extends Component {
  static displayName = 'AddTask';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      functionService: null,
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps(nextProps) {

  }

  selectFunction(fs){
    if(fs == null || fs == undefined)
      return;
    this.setState({
      functionService: fs,
    });
  }

  render() {
    let inputForm = null;
    if(this.state.functionService){
      console.log(this.state.functionService);
      inputForm = <InputForm app={this.state.app} meta={this.state.functionService.metaData.metaDataItemList}/>
    }
    return (
      <div>
        <FunctionServiceBtn visible={true} app={this.state.app} type={this.state.type} select={this.selectFunction.bind(this)}>
          <Button>选择功能</Button>
        </FunctionServiceBtn>
        {inputForm}
      </div>
    );
  }
}
