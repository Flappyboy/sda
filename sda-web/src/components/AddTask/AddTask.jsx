import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import FunctionServiceBtn from "../FunctionService/FunctionServiceBtn";
import InputForm from "../InputForm";
import {doTask} from "../../api";
import WaitTaskDialog from "./WaitTaskDialog";

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
      partition: props.partition,
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps(nextProps) {

  }

  processFs(fs){
    if(fs != null && this.state.type === "Evaluation"){
      if(fs.metaData){
        for(let i=0; i<fs.metaData.metaDataItemList.length; i++){
          if(fs.metaData.metaDataItemList[i].name === "SYS_PARTITION"){

            fs.metaData.metaDataItemList.splice(i,1);
            break;
          }
        }
      }
    }
  }

  clickSelectFunction() {
    this.setState({
      functionService: null,
    });
  }

  selectFunction(fs){
    if(fs == null || fs == undefined)
      return;

    this.processFs(fs);
    this.setState({
      functionService: Object.assign({},fs),
    });
  }

  startTask(values) {
    console.log("partition: ");
    console.log(this.state.partition);
    if(this.state.type === "Evaluation" && this.state.partition != null && this.state.partition != undefined) {
      values.infoValues["SYS_PARTITION"] = [
        {
          id: this.state.partition.id,
          name: "SYS_PARTITION",
        },
      ];
    }
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
    }else{
      this.setState({
        functionService: null,
        task: null,
      })
    }
  }

  onComplete() {
    if(this.props.onComplete){
      this.props.onComplete();
    }else{
      this.onClose();
    }
  }

  render() {
    let wait = null;
    if(this.state.task != null){
      wait = (
        <WaitTaskDialog task={this.state.task} app={this.state.app} onClose={this.onClose.bind(this)} onComplete={this.onComplete.bind(this)}></WaitTaskDialog>
      )
    }
    let inputForm = null;
    if(this.state.functionService){
      console.log(this.state.functionService);
      if(this.state.functionService.metaData)
        inputForm = <InputForm startTask={this.startTask.bind(this)} app={this.state.app} functionService={this.state.functionService}/>
      else{
        inputForm = (
          <div>
            {/*该算法缺少输入数据的元信息，请检查算法后重试。*/}
            This algorithm lacks meta information for input data. Please check the algorithm and try again.
          </div>
        )
      }
    }
    return (
      <div>
        <FunctionServiceBtn visible={this.props.functionVisible} app={this.state.app} type={this.state.type} clickCallback={this.clickSelectFunction.bind(this)} select={this.selectFunction.bind(this)}>
          <Button>Select Function</Button> <span style={{marginLeft: 12}}></span> {this.state.functionService==null ? null : this.state.functionService.desc}
        </FunctionServiceBtn>
        {inputForm}
        {wait}
      </div>
    );
  }
}
