import React, { Component } from 'react';
import { Breadcrumb } from '@alifd/next';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import emitter from './components/ev';

export default class Info extends Component {
  static displayName = 'App';

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

  redirect(path, params){
    this.setState({
      redirect: {
        do: true,
        pathname: path,
        search: jsonToSearh(params),
      },
    });
  }

  render() {
    if (this.state.redirect.do) {
      return (
        <Redirect to={{ pathname: this.state.redirect.pathname, search: this.state.redirect.search }} />
      );
    }
    return (
      <div className="app-page">
        <ColumnForm />
        <SelectableTable app={this.props.app} />
      </div>
    );
  }
}
