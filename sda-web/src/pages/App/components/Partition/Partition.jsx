import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { queryStatisticsList, queryStatistics, delStatistics, addStatistics, addPartition, queryGitList } from '../../../../api';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import moment from 'moment';
import emitter from '../ev';
import DynamicData from './components/DynamicData';

const { Row, Col } = Grid;

const CheckboxGroup = Checkbox.Group;
const FormItem = Form.Item;

const formItemLayout = {
  labelCol: { xxs: 8, s: 3, l: 3 },
  wrapperCol: { s: 12, l: 10 }
};
export default class Partition extends Component {
  static displayName = 'Partition';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      form: {
        factors: [
          { label: 'git数据', value: 'git' },
          { label: '额外条件', value: 'other' },
        ],
      },
    };
  }

  componentDidMount() {
    // 找到锚点
    const anchorElement = document.getElementById('micro-partition');
    // 如果对应id的锚点存在，就跳转到锚点
    if (anchorElement) { anchorElement.scrollIntoView(); }

  }

  componentWillReceiveProps(nextProps) {
    this.state = {
      app: nextProps.app,

      form: {
        dynamic: undefined,
        factors: [
          { label: 'git数据', value: 'git' },
          { label: '额外条件', value: 'other' },
        ],
      },
    };
    this.setState({});

    this.componentDidMount();
  }

  reset = () => {
  }

  formChange = (values, field) => {
    console.log(values, field)
  }

  submit = (values, errors) => {
    console.log('error', errors, 'value', values);
    if (!errors) {
      const param = {
        appid: this.state.app.id,
        algorithmsid: 1,
        dynamicanalysisinfoid: values.dynamic,
        type: 0,
      };
      addPartition(param).then((response) => {
        console.log(response.data.data);
        this.setState({
          // redirectToPartitionParam: id,
          redirectToPartition: true,
        });
      })
        .catch((error) => {
          console.log(error);
        });
    } else {
      // 处理表单报错
    }
  };

  selectDynamic = (item) => {
    if (item) {
      this.state.form.dynamic = item.id;
      this.state.selectedDynamic = item;
    } else {
      this.state.form.dynamic = '';
      this.state.selectedDynamic = null;
    }
    this.setState({});
  }


  render() {
    if (this.state.redirectToPartition) {
      return (
        <Redirect to={{ pathname: '/partition', search: `?addAppId=${this.state.app.id}` }} />
      );
    }
    const clearDynamicButton = <Button type="normal" onClick={() => { this.selectDynamic(); }} >clear</Button>;
    const title = `${this.state.app.name} 微服务划分`;
    return (
      <div id="micro-partition" className="grouped-form">
        <IceContainer title={title} style={styles.container}>
          <Form onChange={this.formChange} value={this.state.form}>
            <div>
              <div style={styles.subForm}>
                <h3 style={styles.formTitle}>动态分析数据</h3>
                <div>
                  <FormItem label="已选择  " {...formItemLayout}>
                    <Input name="dynamic" addonAfter={clearDynamicButton} readOnly />
                  </FormItem>
                  <DynamicData app={this.state.app}
                    selected={this.state.selectedDynamic}
                    selectCallback={this.selectDynamic}
                  />
                </div>
              </div>

              <div style={styles.subForm}>
                <h3 style={styles.formTitle}>其他因素</h3>
                <div>
                  <FormItem label="因素选择:  " {...formItemLayout}>
                    <CheckboxGroup
                      itemDirection="ver"
                      name="deliveryType"
                      defaultValue={['git']}
                      dataSource={this.state.form.factors}
                      defaultChecked
                    />
                    <Input name="filter" placeholder="输入筛选条件"/>
                  </FormItem>
                </div>
              </div>

              <FormItem label=" " {...formItemLayout}>
                <Form.Submit type="primary" validate onClick={this.submit}>
                  立即划分
                </Form.Submit>
                <Form.Reset style={styles.resetBtn} onClick={this.reset}>
                  重置
                </Form.Reset>
              </FormItem>
            </div>
          </Form>
        </IceContainer>
      </div >
    );
  }
}

const styles = {
  container: {
    paddingBottom: 0,
  },
  subForm: {
    marginBottom: '20px',
  },
  formTitle: {
    margin: '0 0 20px',
    paddingBottom: '10px',
    fontSize: '14px',
    borderBottom: '1px solid #eee',
  },
  formItem: {
    height: '28px',
    lineHeight: '28px',
    marginBottom: '25px',
  },
  formLabel: {
    textAlign: 'right',
  },
  btns: {
    margin: '25px 0',
  },
  resetBtn: {
    marginLeft: '20px',
  },
};
const propsConf = {
  className: 'next-myclass',
  style: { background: 'gray', color: 'white' },
  // onDoubleClick: () => {
  //   console.log('doubleClicked');
  // }
};
