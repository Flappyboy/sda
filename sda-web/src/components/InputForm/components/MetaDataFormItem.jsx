import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid, Card } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import SdaUpload from "./SdaUpload";
import SdaInput from "./SdaInput";
import SdaDateTime from "./SdaDateTime";
import InfoInputTable from "./InfoInputTable";

const { Row, Col } = Grid;
const commonProps = {
  style: { width: 300 },
  title: 'Title',
  subTitle: 'Sub-title'
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
    this.props.formCallback(name, value);
  }

  infoCallback(name, value){
    this.props.infoCallback(name, value);
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
          alert("Unsupport Type " + form);
      }
    }else if(this.state.metaDataItem.dataType == "InfoData") {
      form=(
        <InfoInputTable app={this.state.app}
                        name={this.state.metaDataItem.name}
                        type={this.state.metaDataItem.type}
                        meta={this.state.metaDataItem}
                        callback={this.infoCallback.bind(this)}/>
        );
    }
    return (
      <Row>
        <Col span="24">
          {/*<Row>*/}
            {/*{this.state.name}*/}
          {/*</Row>*/}
          <Row>
            {form}
          </Row>
        </Col>
      </Row>
    );
  }
}
