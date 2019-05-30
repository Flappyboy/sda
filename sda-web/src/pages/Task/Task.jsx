import React, { Component } from 'react';
import { Breadcrumb } from '@alifd/next';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';

export default class Task extends Component {
  static displayName = 'Task';

  constructor(props) {
    super(props);

    this.state = {
      redirect: {do: false},
      app: null,
    };
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
        <ColumnForm />
        <SelectableTable/>
      </div>
    );
  }
}
