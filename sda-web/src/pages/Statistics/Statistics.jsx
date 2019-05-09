import React, { Component } from 'react';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import DetailTable from './components/DetailTable';

export default class Statistics extends Component {
  static displayName = 'Statistics';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
    };
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      app: nextProps.app,
    });
  }

  render() {
    if (this.state.app) {
      return (
        <div className="statistics-page">
          <SelectableTable app={this.state.app} />
          <DetailTable />
        </div>
      );
    }
    return (
      <div className="statistics-page">
        <ColumnForm />
        <SelectableTable />
        <DetailTable />
      </div>
    );
  }
}
