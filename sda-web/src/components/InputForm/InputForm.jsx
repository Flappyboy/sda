import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import MetaDataFormItem from "./components/MetaDataFormItem";


const { Row, Col } = Grid;

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;

const formItemLayout = {
  // labelCol: { xxs: 8, s: 3, l: 3 },
  // wrapperCol: { s: 12, l: 10 }
};
export default class InputForm extends Component {
  static displayName = 'InputForm';

  constructor(props) {
    super(props);
    this.values = {
      formValues: {},
      infoValues: {},
    };
    this.state = {
      app: props.app,
      functionService: props.functionService,
      meta: props.functionService.metaData.metaDataItemList,
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.functionService != this.props.functionService) {
      this.values.formValues = {};
      this.values.infoValues = {};
      this.setState({
        app: nextProps.app,
        functionService: nextProps.functionService,
        meta: nextProps.functionService.metaData.metaDataItemList,
      });
    }
  }

  formCallback(name, value){
    this.values.formValues[name] = value;
  }
  infoCallback(name, value){
    this.values.infoValues[name] = value;
  }

  startTask(){
    this.props.startTask(this.values);
  }

  render() {
    return (
      <div {...formItemLayout}>
        {this.state.meta.map((metaDataItem) => (
          <MetaDataFormItem app={this.state.app}
                            metaDataItem={metaDataItem}
                            formCallback={this.formCallback.bind(this)}
                            infoCallback={this.infoCallback.bind(this)}/>
        ))}
        <Button justify="center" style={{width:'1010px'}} onClick={this.startTask.bind(this)}>执行</Button>
      </div>
    );
  }
}
