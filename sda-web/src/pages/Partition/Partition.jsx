import React, { Component } from 'react';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import PartitionDetail from './components/PartitionDetail';

export default class Partition extends Component {
  static displayName = 'Partition';

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className="partition-page">
        <ColumnForm />
        <SelectableTable search={this.props.location.search}/>
        <PartitionDetail />
      </div>
    );
  }
}
