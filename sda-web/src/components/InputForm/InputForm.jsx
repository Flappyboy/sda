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
    this.formValues = {};
    this.infoValues = {};
    this.state = {
      app: props.app,
      meta: props.meta,
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.meta != this.props.meta) {
      this.setState({
        app: nextProps.app,
        meta: nextProps.meta,
      });
      this.formValues = {};
    }
  }

  formCallback(name, value){
    this.formValues[name] = value;
    console.log(this.formValues);
  }
  infoCallback(name, value){
    this.infoValues[name] = value;
    console.log(this.infoValues);
  }

  doTask() {

  }

  render() {
    return (
      <div {...formItemLayout}>
        <br/>
        {this.state.meta.map((metaDataItem) => (
          <MetaDataFormItem app={this.state.app}
                            metaDataItem={metaDataItem}
                            formCallback={this.formCallback.bind(this)}
                            infoCallback={this.infoCallback.bind(this)}/>
        ))}
        <Button onClick={this.doTask.bind(this)}>执行</Button>
      </div>
    );
  }
}
