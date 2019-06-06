import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  FormBinderWrapper as IceFormBinderWrapper,
  FormBinder as IceFormBinder,
} from '@icedesign/form-binder';
import { Input, Button, Select, Grid } from '@alifd/next';

import emitter from '../ev';

const { Row, Col } = Grid;

export default class ColumnForm extends Component {
  static displayName = 'ColumnForm';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      value: {
        desc: '',
      },
    };
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

  render() {
    return (
      <div className="column-form">
        <IceContainer title="App" style={styles.container}>
          <IceFormBinderWrapper
            ref={(formRef) => {
              this.formRef = formRef;
            }}
            value={this.state.value}
            onChange={this.onFormChange}
          >
            <div>
              <Row wrap>
                <Col xxs="24" s="10" l="10">
                  <Row style={styles.formItem}>
                    <Col xxs="8" s="6" l="4" style={styles.formLabel}>
                      App
                    </Col>

                    <Col offset="1" s="13" l="13">
                      <IceFormBinder
                        name="name"
                      >
                        <Input style={{ width: '100%' }} />
                      </IceFormBinder>
                    </Col>
                    <Col xxs="8" s="6" l="4" style={styles.formLabel}>
                      Description
                    </Col>

                    <Col offset="1" s="13" l="13">
                      <IceFormBinder
                        name="desc"
                      >
                        <Input style={{ width: '100%' }} />
                      </IceFormBinder>
                    </Col>
                  </Row>
                </Col>
              </Row>
              <Row style={styles.btns}>
                <Col xxs="8" s="2" l="2" style={styles.formLabel}>
                  {' '}
                </Col>
                <Col s="12" l="10">
                  <Button type="primary" onClick={this.submit}>
                    Query
                  </Button>
                  <Button style={styles.resetBtn} onClick={this.reset}>
                    Reset
                  </Button>
                </Col>
              </Row>
            </div>
          </IceFormBinderWrapper>
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
