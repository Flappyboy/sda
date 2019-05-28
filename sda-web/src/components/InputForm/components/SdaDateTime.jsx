import { Input, DatePicker, TimePicker } from '@alifd/next';
import React, { Component} from 'react';
import '../../../api';

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
      <div>
        <DatePicker onChange={this.setDate.bind(this)} />
        <TimePicker onChange={this.setTime.bind(this)} />
      </div>
    );
  }
}
