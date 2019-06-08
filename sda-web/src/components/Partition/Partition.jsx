import React, { Component } from 'react';
import SelectableTable from './components/SelectableTable';
import ColumnForm from './components/ColumnForm';
import PartitionDetail from './components/PartitionDetail';
import EvaluationCompare from "./components/EvaluationCompare/EvaluationCompare";

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
        <SelectableTable app={this.props.app}/>
        <EvaluationCompare app={this.props.app}/>
        <PartitionDetail app={this.props.app}/>
      </div>
    );
  }
}
