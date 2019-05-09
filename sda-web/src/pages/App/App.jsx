import React, { Component } from 'react';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import Partition from './components/Partition';
import Statistics from '../Statistics/Statistics';
import emitter from './components/ev';

export default class App extends Component {
  static displayName = 'App';

  constructor(props) {
    super(props);
    this.state = {
      attach: null,
      app: null,
    };
  }
  componentDidMount() {
    // 声明一个自定义事件
    // 在组件装载完成以后
    this.eventEmitter = emitter.addListener('show_attach', this.showAttach);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('show_attach', this.showAttach);
  }

  

  render() {
    let attach = null;
    switch (this.state.attach) {
      case 'partition':
        attach = <Partition app={this.state.app} />;
        break;
      case 'dynamic':
        attach = <Statistics app={this.state.app} />;
        break;
      case 'git':

        break;
      default:
        attach = null;
    }
    return (
      <div className="app-page">
        <ColumnForm />
        <SelectableTable />
        {attach}
      </div>
    );
  }

  showAttach = (target, app) => {
    this.setState({
      attach: target,
      app,
    });
  }
}
