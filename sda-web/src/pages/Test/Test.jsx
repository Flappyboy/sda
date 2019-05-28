import React, { Component } from 'react';
import { Button } from '@alifd/next';
import {FunctionServiceBtn} from "../../components/FunctionService";
import {AddTaskConfirmDialog, AddTaskDialogBtn} from "../../components/AddTask";
import AppDetail from "../../components/AppDetail";

export default class Test extends Component {
  static displayName = 'Test';

  constructor(props) {
    super(props);
    this.app={
      id: 1,
      name: 'test'
    }
  }
  componentDidMount() {
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
  }

  selectFunction(select) {
    alert("select: "+select.name);
  }

  nothing(){

  }

  render() {
    return (
      <div>
        <FunctionServiceBtn app={this.app} type="InfoCollection" select={this.selectFunction}>
          <Button>
            functionService
          </Button>
        </FunctionServiceBtn>
        {/*<AddTaskConfirmDialog app={this.app} type="InfoCollection" onClose={this.nothing}>
          应用已添加成功，
          是否向应用中添加信息？
        </AddTaskConfirmDialog>*/}
        <br/>
        <AddTaskDialogBtn app={this.app} type="InfoCollection">
          <Button>
            添加功能
          </Button>
        </AddTaskDialogBtn>
        <AppDetail app={{id: 1, name: 'test'}} />
      </div>
    );
  }
}
