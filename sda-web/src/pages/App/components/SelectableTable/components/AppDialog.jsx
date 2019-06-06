import React, { Component } from 'react';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
import '../../../../../api';

const FormItem = Form.Item;

export default class AppDialog extends Component {
  static displayName = 'AppDialog';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      app: props.app? props.app: {name: '', desc: ''},
    };
    this.field = new Field(this);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      visible: nextProps.visible,
    });
  }

  onOk = (values) => {
    this.field.validate((errors, values) => {
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }
      return this.props.onOk(values);
    });
  };

  render() {
    const action = `${global.base.baseLocation}/upload`;
    const formItemLayout = {
      labelCol: {
        fixedSpan: 6,
      },
      wrapperCol: {
        span: 14,
      },
    };
    return (
        <Dialog
          style={{ width: 640 }}
          visible={this.props.visible}
          onOk={this.onOk}
          okProps={this.props.okProps}
          onCancel={this.props.onCancel}
          onClose={this.props.onClose}
          title={this.props.title}
        >
          <Form field={this.field} >
            <FormItem
              label="App Name："
              required
              {...formItemLayout}
            >
              <Input defaultValue={this.state.app ? this.state.app.name : ""} name="name" />
            </FormItem>

            <FormItem
              label="Description："
              {...formItemLayout}
            >
              <Input defaultValue={this.state.app ? this.state.app.desc : ""} name="desc" />
            </FormItem>
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
