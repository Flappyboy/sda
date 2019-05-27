import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import SdaUpload from "./SdaUpload";
import SdaInput from "./SdaInput";
import SdaDateTime from "./SdaDateTime";
import InfoInputTable from "./InfoInputTable";

const { Row, Col } = Grid;

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;

const formItemLayout = {
  labelCol: { xxs: 8, s: 3, l: 3 },
  wrapperCol: { s: 12, l: 10 }
};
export default class MetaDataFormItem extends Component {
  static displayName = 'MetaDataFormItem';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      metaDataItem: props.metaDataItem,
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.metaDataItem != this.state.metaDataItem){
      this.setState({
        metaDataItem: nextProps.metaDataItem,
      })
    }
  }

  formCallback(name, value){
    this.props.callback(name, value);
  }

  infoCallback(name, value){
    this.props.callback(name, value);
  }

  render() {
    let form = null;
    if(this.state.metaDataItem.dataType == "FormData") {
      switch (this.state.metaDataItem.type) {
        case 'STRING':
          form = (
            <SdaInput name={this.state.metaDataItem.name} callback={this.formCallback.bind(this)}/>
          );
          break;
        case 'TIMESTAMP':
          form = (
            <SdaDateTime name={this.state.metaDataItem.name} callback={this.formCallback.bind(this)}/>
          );
          break;
        case 'FILE':
          form = (
            <SdaUpload name={this.state.metaDataItem.name} callback={this.formCallback.bind(this)}/>
          );
          break;
        default:
          alert("不支持的类型 " + form);
      }
    }else if(this.state.metaDataItem.dataType == "InfoData") {
      form=(
        <InfoInputTable app={this.state.app}
                         name={this.state.metaDataItem.name}
                        type={this.state.metaDataItem.type}
                        callback={this.infoCallback.bind(this)}/>
        );
    }
    return (
        <div>
          {this.state.metaDataItem.name}
          <br/>
          {form}
        </div>
    );
  }
}
