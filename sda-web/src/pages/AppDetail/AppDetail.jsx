import React, { Component } from 'react';
import { Tab } from '@alifd/next';
import SelectableTable from './components/SelectableTable';
import emitter from './components/ev';

export default class AppDetail extends Component {
  static displayName = 'AppDetail';

  constructor(props) {
    super(props);
    this.state = {
      app: null,
    };
  }
  componentDidMount() {
    // this.eventEmitter = emitter.addListener('show_attach', this.showAttach);
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    // emitter.removeListener('show_attach', this.showAttach);
  }

  

  render() {
    return (
      <Tab>
        <Tab.Item title="PartitionInfo" key="2">
          <SelectableTable />
        </Tab.Item>
        <Tab.Item title="Info" key="3">
          <SelectableTable />
        </Tab.Item>
      </Tab>
    );
  }

  showAttach = (target, app) => {
    this.setState({
      attach: target,
      app,
    });
  }
}
