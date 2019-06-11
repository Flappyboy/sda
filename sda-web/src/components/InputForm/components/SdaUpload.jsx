import { Upload, Button, Grid } from '@alifd/next';
import React, { Component } from 'react';
import '../../../api';

// const { Core } = Upload;
const { Row, Col } = Grid;

export default class SdaUpload extends Component {
  constructor(props) {
    super(props);

    this.callback = (arg) => { props.callback(props.name, arg); };

    this.state = {
      disabled: false,
      dragable: true,
    };

    /* eslint-disable */
    ["onDisabledHandler", "onDragableHandler", "onAbortHandler"].map(fn => {
      this[fn] = this[fn].bind(this);
    });
    /* eslint-enable */
  }

  onDisabledHandler() {
    this.setState({
      disabled: !this.state.disabled,
    });
  }

  onDragableHandler() {
    this.setState({
      dragable: !this.state.dragable,
    });
  }

  onAbortHandler() {
    this.refs.inner.abort();
  }

  onRemove(){
    this.callback([]);
    return true;
  }

  render() {
    const action = `${global.base.baseLocation}/upload`;
    const download = `${global.base.baseLocation}/download`;
    return (
      <div style={{marginTop:"20px"}}>
        {/* <Upload
          style={{
            display: 'block',
            textAlign: 'center',
            width: '200px',
            height: '150px',
            lineHeight: '150px',
            border: '1px dashed #aaa',
            borderRadius: '5px',
            fontSize: '12px',
          }}
          limit={1}
          listType="text"
          action={action}
          accept=""
          name="file"
          disabled={this.state.disabled}
          dragable={this.state.dragable}
          beforeUpload={this.beforeUpload}
          onStart={this.onStart}
          onProgress={this.onProgress}
          onSuccess={this.onSuccess}
          onError={this.onError}
          onAbort={this.onAbort}
        >
          {this.state.disabled ? '禁止上传' : this.state.dragable ? '点击或者拖拽上传' : '点击上传'}
        </Upload> */}
        <Row>
          <Col fixedSpan="6" align='center'>
            <font size="3">{this.props.name}</font>
          </Col>
          <Col span="18">
        {/*{this.props.name}*/}
            <Upload
              action={action}
              limit={1}
              listType="text"
              onSuccess={this.onSuccess}
              onError={this.onError}
              name="file"
              accept=""
              onRemove={this.onRemove.bind(this)}
              formatter={(res, file) => {
                // 函数里面根据当前服务器返回的响应数据
                // 重新拼装符合组件要求的数据格式
                const result = {
                  success: true,
                  path: res.path,
                  url: `${download}?path=${res.path}`,
                };
                return result;
              }}
            >
              {/*<font size="3" align='center'>上传文件：</font>*/}
              <Button type="primary"
                      align="center"
                      // style={{ margin: '0 0 10px' }}
              >
                Upload
              </Button>
            </Upload>
          </Col>
        </Row>
        {/* <br />
        <div>
          <Button type="primary" onClick={this.onDisabledHandler}>
            切换 disabled 状态
          </Button>&nbsp;
          <Button type="primary" onClick={this.onDragableHandler}>
            切换 dragable 状态
          </Button>&nbsp;
          <Button type="primary" onClick={this.onAbortHandler}>
            中断全部上传
          </Button>
        </div> */}
      </div>
    );
  }

  beforeUpload = (file) => {
    console.log('beforeUpload callback : ', file);
  }

  onStart = (files) => {
    console.log('onStart callback : ', files);
  }

  onProgress = (e, file) => {
    console.log('onProgress callback : ', e, file);
  }

  onSuccess = (res, file) => {
    console.log('onSuccess callback : ', res, file);
    const list = [];
    list.push(res.response.path);
    this.callback(list);
  }

  onError = (err, res, file) => {
    console.log('onError callback : ', err, res, file);
  }

  onAbort = (e, file) => {
    console.log('onAbort callback : ', e, file);
  }
}
