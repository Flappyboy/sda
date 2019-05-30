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
      <div style={{marginTop:"10px"}}>
        <Row>
          <Col span="3" align='center'><font size="3" style={{marginBottom:"0px"}}>选择时间：</font></Col>
          <Col span="12"><DatePicker style={{width:'288px'}} onChange={this.setDate.bind(this)} /></Col>
          <Col span="9" fixedOffset="4"><TimePicker onChange={this.setTime.bind(this)} /></Col>
        </Row>
      </div>
    );
  }
}
