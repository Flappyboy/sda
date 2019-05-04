import React, { Component } from 'react';
import { addApp } from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@icedesign/base';
import { Upload } from '@alifd/next';
// import My from './My';
import '../../../../../api';

const FormItem = Form.Item;

export default class AddDialog extends Component {
  static displayName = 'EditDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      filepath: '',
    };
    this.field = new Field(this);
  }

  handleSubmit = () => {
    this.field.validate((errors, values) => {
      console.log(values);
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }
      values.path = this.state.filepath;
      addApp(values).then((response) => {
        console.log(response.data.data);
        this.props.addNewItem(response.data.data);

        this.setState({
          visible: false,
        });
      })
        .catch((error) => {
          console.log(error);
        });
    });
  };

  onOpen = () => {
    this.setState({
      visible: true,
    });
  };

  onClose = () => {
    this.setState({
      visible: false,
    });
  };

  callback = (filepath) => {
    this.setState({
      filepath,
    });
  }

  render() {
    const init = this.field.init;
    const formItemLayout = {
      labelCol: {
        fixedSpan: 6,
      },
      wrapperCol: {
        span: 14,
      },
    };
    const action = `${global.base.baseLocation}/upload`;
    return (
      <div style={styles.editDialog}>
        <Button
          size="small"
          type="primary"
          onClick={() => this.onOpen()}
        >
          增加
        </Button>
        <Dialog
          style={{ width: 640 }}
          visible={this.state.visible}
          onOk={this.handleSubmit}
          closable="esc,mask,close"
          onCancel={this.onClose}
          onClose={this.onClose}
          title="添加应用"
        >
          <Form direction="ver" field={this.field}>
            <FormItem label="应用名：" {...formItemLayout}>
              <Input
                {...init('name', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            {/* <FormItem label="开始时间：" {...formItemLayout}>
              <Input
                {...init('startTime', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem> */}


            <FormItem label="描述：" {...formItemLayout}>
              <Input
                {...init('desc', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>
            <FormItem label="上传JAR/WAR" {...formItemLayout}>
              {/* <My callback={this.callback.bind(this)} /> */}
              <Upload
                action={action}
                limit={1}
                listType="text"
                onSuccess={this.onSuccess}
                onError={this.onError}
                name="file"
                accept=""
                formatter={(res, file) => {
                  // 函数里面根据当前服务器返回的响应数据
                  // 重新拼装符合组件要求的数据格式
                  console.log('lalala');
                  console.log(res);
                  const result = {
                    success: res.status === 200,
                    url: res.data.path,
                    message: res.msg,
                  };
                  console.log(result);
                  return result;
                }}
              >
                <Button type="primary" style={{ margin: '0 0 10px' }}>点击上传</Button>
              </Upload>
            </FormItem>

          </Form>

        </Dialog>
      </div>
    );
  }
  onSuccess = (res, file) => {
    console.log('onSuccess callback : ', res, file);
    this.setState({
      filepath: res.url,
    });
  }

  onError = (err, res, file) => {
    console.log('onError callback : ', err, res, file);
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
    marginRight: '5px',
  },
};
