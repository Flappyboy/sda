import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import FunctionServiceBtn from "../FunctionService/FunctionServiceBtn";
import InputForm from "../InputForm";
import {doTask} from "../../api";
import WaitTaskDialog from "./WaitTaskDialog";

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
      functionService: props.functionService ? props.functionService : null,
      task: null,
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

  startTask(values) {
    const params = {
      appId: this.state.app.id,
      type: this.state.type,
      functionName: this.state.functionService.name,
      inputDataDto: {
        infoDatas: values.infoValues,
        formDatas: values.formValues,
      }
    };
    doTask(params).then((response) => {
      this.setState({
        task: response.data,
      });
    })
      .catch((error) => {
        console.log(error);
      });
  }

  onClose(){
    if(this.props.onClose){
      this.props.onClose();
    }
  }

  render() {
    let wait = null;
    if(this.state.task != null){
      wait = (
        <WaitTaskDialog task={this.state.task} app={this.state.app} onClose={this.onClose.bind(this)}></WaitTaskDialog>
      )
    }
    let inputForm = null;
    if(this.state.functionService){
      console.log(this.state.functionService);
      if(this.state.functionService.metaData)
        inputForm = <InputForm startTask={this.startTask.bind(this)} app={this.state.app} meta={this.state.functionService.metaData.metaDataItemList}/>
      else{
        inputForm = (
          <div>
            该算法缺少输入数据的元信息，请检查算法后重试。
          </div>
        )
      }
    }
    return (
      <div>
        <FunctionServiceBtn visible={this.props.functionVisible} app={this.state.app} type={this.state.type} select={this.selectFunction.bind(this)}>
          <Button>选择功能</Button>
        </FunctionServiceBtn>
        {inputForm}
        {wait}
      </div>
    );
  }
}
