import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import {doTask, evaluationLastForIds} from "../../../../api";

const formItemLayout = {
  labelCol: { xxs: 8, s: 3, l: 3 },
  wrapperCol: { s: 12, l: 10 }
};
export default class EvaluationCompare extends Component {
  static displayName = 'EvaluationCompare';

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
    return (
      <div>

      </div>
    );
  }
}
