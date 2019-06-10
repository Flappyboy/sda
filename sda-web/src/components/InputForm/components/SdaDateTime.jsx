import { Input, DatePicker, TimePicker, Grid } from '@alifd/next';
import React, { Component} from 'react';
import '../../../api';

const { Row, Col } = Grid;

export default class SdaDateTime extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.callback(props.name, arg); };
    this.state = {

    };
    this.date = null;
    this.time = null;
  }

  setDate(date) {
    this.date = date;
    this.setDateTime();
  }

  setTime(time) {
    this.time = time;
    this.setDateTime();
  }

  setDateTime(){
    if(this.date == null || this.time == null){
      this.callback(null);
      return;
    }
    const list = [];
    list.push(this.date.hours(this.time.hours()).minutes(this.time.minutes()).seconds(this.time.seconds()).valueOf());
    this.callback(list);
  }

  render() {
    return (
      <div style={{marginTop:"20px"}}>
        <Row>
          <Col fixedSpan="6" align='center'><font size="3">{this.props.name}</font></Col>
          <Col span="11"><DatePicker onChange={this.setDate.bind(this)} /></Col>
          <Col span="7" fixedOffset="1"><TimePicker onChange={this.setTime.bind(this)} /></Col>
        </Row>
      </div>
    );
  }
}
