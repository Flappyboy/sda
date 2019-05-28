import React, { Component } from 'react';
import { Breadcrumb } from '@alifd/next';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import AppDetail from "../../components/AppDetail";

export default class AppDetailPage extends Component {
  static displayName = 'AppDetailPage';

  constructor(props) {
    super(props);
  }
  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    // this.eventEmitter = emitter.addListener('show_attach', this.showAttach);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    // emitter.removeListener('show_attach', this.showAttach);
  }

  render() {
    return (
      <div>
        <AppDetail location={this.props.location}/>
      </div>
    );
  }
}
