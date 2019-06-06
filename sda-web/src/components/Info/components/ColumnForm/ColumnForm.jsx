import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  FormBinderWrapper as IceFormBinderWrapper,
  FormBinder as IceFormBinder,
} from '@icedesign/form-binder';
import { Input, Button, Select, Grid } from '@alifd/next';

import emitter from '../ev';
import {queryInfoTypes} from "../../../../api";

const { Row, Col } = Grid;

export default class ColumnForm extends Component {
  static displayName = 'ColumnForm';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      selectState: "loading",
      data: {},
      selected: null,
    };
  }

  componentDidMount() {
    queryInfoTypes().then((response) => {
      this.setState({
        data: response.data,
        selectState: "",
      })
    }).catch((error) => {
      alert(error);
      this.setState({
        selectState: "",
      })
    })
  }

  onFormChange = (value) => {
    this.setState({
      value,
    });
  };

  reset = () => {
    this.setState({
      value: {
        desc: '',
      },
    });
  };

  query(){
    const value={
      name: this.state.selected,
    };
    emitter.emit('query_infos', value);
  }

  submit = () => {
    this.formRef.validateAll((error, value) => {
      // console.log('error', error, 'data', data);
      if (error) {
        // 处理表单报错
      }
      if(value.name)
        value.name = value.name.trim();
      if(value.desc)
        value.desc = value.desc.trim();
      emitter.emit('query_apps', value);
    });
  };

  onChange = (v) => {
    this.state.selected = v;
    this.setState({
      selectState: "",
      selected: v,
    });
    if(v) {
      if (v in this.state.data.nameDescMap) {
        this.setState({
          selectState: "",
        })
        this.query();
      } else {
        this.setState({
          selectState: "error",
        })
      }
    }
  };

  render() {
    let desc = null;
    console.log(this.state.selected);
    console.log(this.state.selectState);

    if(this.state.selected && !this.state.selectState) {
        desc = (
          <span style={{marginLeft: 10}}>
        {/*<Button onClick={this.query.bind(this)}>查询</Button>*/}
        <span style={{marginLeft: 10}}> Description： {this.state.data.nameDescMap[this.state.selected]} </span>
        </span>
        );
    }
    return (
      <div className="column-form">
        <IceContainer title="Info Query" style={styles.container}>
          <Select.AutoComplete hasClear
                               style={{width: 300}}
                               onChange={this.onChange}
                               state={this.state.selectState}
                               dataSource={this.state.data.types} />
          {desc}
        </IceContainer>
      </div>
    );
  }
}

const styles = {
  container: {
    paddingBottom: 0,
  },
  formItem: {
    height: '28px',
    lineHeight: '28px',
    marginBottom: '10px',
  },
  formLabel: {
    textAlign: 'right',
  },
  btns: {
    margin: '5px 0',
  },
  resetBtn: {
    marginLeft: '20px',
  },
};
