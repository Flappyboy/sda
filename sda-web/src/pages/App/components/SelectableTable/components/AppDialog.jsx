import React, { Component } from 'react';
import { addApp, updateApp } from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
// import My from './My';
import '../../../../../api';

const FormItem = Form.Item;

export default class AppDialog extends Component {
  static displayName = 'AppDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      type: props.type,
      app: props.app,
    };
    this.field = new Field(this);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      app: nextProps.app,
    })
  }

  handleSubmit = () => {
    this.field.validate((errors, values) => {
      console.log(values);
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }
      this.setState({
        loading: true,
      })
      if(this.state.type == "add"){
        addApp(values).then((response) => {
          console.log(response.data.data);
          this.props.addCallback(response.data.data);

        })
          .catch((error) => {
            console.log(error);
          }).finally(() => {
          this.setState({
            visible: false,
            loading: false,
          });
        });
      }else if(this.state.type == "update"){
        values.id = this.state.app.id;
        updateApp(values).then((response) => {
          console.log(response.data.data);
          this.props.updateCallback(response.data.data);
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
          this.setState({
            visible: false,
            loading: false,
          })
        });
      }

    });
  };

  onOpen = () => {
    this.setState({
      visible: true,
    });
  };

  onClose = () => {
    this.props.close();
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

    var title = "应用";
    if(this.state.type == "add"){
      title = "添加" + title;
    }else if(this.state.type == "update"){
      title = "编辑" + title;
    }
    return (
        <Dialog
          style={{ width: 640 }}
          visible={this.props.visible}
          onOk={this.handleSubmit}
          onCancel={this.onClose}
          onClose={this.onClose}
          title={title}
        >
          <Form direction="ver" field={this.field}>

            <FormItem
              label="应用名："
              required
            >
              <Input defaultValue={this.state.app ? this.state.app.name : ""} name="name" />
            </FormItem>

            <FormItem
              label="描述："
              required
            >
              <Input defaultValue={this.state.app ? this.state.app.desc : ""} name="desc" />
            </FormItem>

            {/* <FormItem label="开始时间：" {...formItemLayout}>
              <Input
                {...init('startTime', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem> */}


            {/*<FormItem label="描述：" {...formItemLayout}>
              <Input value={this.state.app.desc}
                {...init('desc', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>*/}
           {/* <FormItem label="上传JAR/WAR" {...formItemLayout}>
               <My callback={this.callback.bind(this)} />
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
            </FormItem>*/}

          </Form>

        </Dialog>
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
