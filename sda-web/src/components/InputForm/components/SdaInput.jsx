import { Input, Grid } from '@alifd/next';
import React, { Component } from 'react';
import '../../../api';

// const { Core } = Upload;
const { Row, Col } = Grid;

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
      <Row style={{marginTop:"10px"}}>
        <Col span="10">
          {this.props.name}
        </Col>
        <Col span="14">
          <Input
            // style={{width:"605px"}}
            onChange={this.setValue.bind(this)}
            trim/>
        </Col>
      </Row>
    );
  }
}
