import { Input } from '@alifd/next';
import React, { Component } from 'react';
import '../../../api';

// const { Core } = Upload;

export default class SdaInput extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.callback(props.name, arg); };

    this.state = {

    };
  }

  setValue(value) {
    const list = [];
    list.push(value);
    this.callback(list);
  }

  render() {
    return (
      <div style={{marginTop:"10px"}}>
        <font size="3" style={{marginBottom:"0px"}}>输入字符：</font>
        <Input size="large" style={{width:"605px"}} name={this.props.name} onChange={this.setValue.bind(this)} trim/>
      </div>
    );
  }
}
