import React, { Component } from 'react';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import EvaluationCompare from "./EvaluationCompare";

export default class EvaluationCompareDialog extends Component {
  static displayName = 'EvaluationCompareDialog';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      type: props.type,
      height: window.screen.availHeight*0.8,
      width: window.screen.availWidth > 420 ? window.screen.availWidth*0.7 : window.screen.availWidth,
    };
  }

  componentDidMount() {

  }

  componentWillUnmount() {

  }

  render() {
    return (
      <Dialog title={`Add information ${this.state.app.name}`}
              style={{minHeight: this.state.height, width: this.state.width}}
              visible={this.props.visible}
              isFullScreen={true}
              footer={false}
              onClose={this.props.onClose}
              >
        <EvaluationCompare app={this.state.app} partition={this.props.partition} type={this.state.type} onClose={this.props.onClose} onComplete={this.props.onComplete}/>
      </Dialog>
    );
  }
}
