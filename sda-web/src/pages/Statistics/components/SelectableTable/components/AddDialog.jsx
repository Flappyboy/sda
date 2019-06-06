import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';

const FormItem = Form.Item;

export default class AddDialog extends Component {
  static displayName = 'EditDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      app: props.app,
    };
    this.field = new Field(this);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      app: nextProps.app,
    });
    this.state.app = nextProps.app;
  }

  handleSubmit = () => {
    this.field.validate((errors, values) => {
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }

      // values.createTime = (new Date()).getTime();
      if (this.state.app) {
        values.appid = this.state.app.id;
      }
      this.props.addNewStatistics(values, () => {
        this.setState({
          visible: false,
        });
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
    let appInput = null;
    if (!this.state.app) {
      appInput = (<FormItem label="应用：" {...formItemLayout}>
        <Input
          {...init('appid', {
            rules: [{ required: true, message: '必填选项' }],
          })}
        /> </FormItem>);
    }
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
          title="统计数据"
        >
          <Form direction="ver" field={this.field}>
            {appInput}
            <FormItem label="开始时间：" {...formItemLayout}>
              <Input
                {...init('starttine', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>

            <FormItem label="结束时间：" {...formItemLayout}>
              <Input
                {...init('endtime', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>

            <FormItem label="Description：" {...formItemLayout}>
              <Input
                {...init('desc', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>
          </Form>
        </Dialog>
      </div>
    );
  }
}

const styles = {
  editDialog: {
    display: 'inline-block',
    marginRight: '5px',
  },
};
