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
    this.callback(value);
  }

  render() {
    return (
      <Input name={this.props.name} onChange={this.setValue.bind(this)} trim/>
    );
  }
}
