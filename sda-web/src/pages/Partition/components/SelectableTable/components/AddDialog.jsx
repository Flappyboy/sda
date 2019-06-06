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
    };
    this.field = new Field(this);
  }

  handleSubmit = () => {
    this.field.validate((errors, values) => {
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }

      values.createTime = (new Date()).getTime();
      this.props.addNewStatistics(values);

      this.setState({
        visible: false,
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
            <FormItem label="应用：" {...formItemLayout}>
              <Input
                {...init('app', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="开始时间：" {...formItemLayout}>
              <Input
                {...init('startTime', {
                  rules: [{ required: false }],
                })}
              />
            </FormItem>

            <FormItem label="结束时间：" {...formItemLayout}>
              <Input
                {...init('endTime', {
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
