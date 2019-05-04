import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  FormBinderWrapper as IceFormBinderWrapper,
  FormBinder as IceFormBinder,
  FormError as IceFormError,
} from '@icedesign/form-binder';
import { Input, Button, Select, Grid } from '@icedesign/base';

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
      // console.log('error', error, 'value', value);
      if (error) {
        // 处理表单报错
      }
      emitter.emit('query_apps', 'Hello');
    });
  };

  render() {
    return (
      <div className="column-form">
        <IceContainer title="应用" style={styles.container}>
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
                      应用
                    </Col>

                    <Col offset="1" s="13" l="13">
                      <IceFormBinder
                        name="appName"
                      >
                        <Input style={{ width: '100%' }} />
                      </IceFormBinder>
                    </Col>
                    <Col xxs="8" s="6" l="4" style={styles.formLabel}>
                      描述
                    </Col>

                    <Col offset="1" s="13" l="13">
                      <IceFormBinder
                        name="contractId"
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
                    查询
                  </Button>
                  <Button style={styles.resetBtn} onClick={this.reset}>
                    重置
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
