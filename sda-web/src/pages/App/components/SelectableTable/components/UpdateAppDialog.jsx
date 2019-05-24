import React, { Component } from 'react';
import { addApp } from '../../../../../api';
import { Dialog, Button, Form, Input, Field } from '@alifd/next';
import { Upload } from '@alifd/next';
// import My from './My';
import '../../../../../api';
import AppDialog from "./AppDialog";

const FormItem = Form.Item;

export default class AddAppDialog extends Component {
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
        <AppDialog/>
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
