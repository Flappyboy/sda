import React, { Component } from 'react';
import { Tab, Loading } from '@alifd/next';
import IceContainer from '@icedesign/container';
import { BrowserRouter as Router, Route, Link, Redirect, withRouter } from 'react-router-dom';
import emitter from './components/ev';

import {getParams} from "../../utils/url";
import {queryApp} from "../../api";
import {AddTask} from "../AddTask";
import Partition from "../Partition";
import Info from "../Info";

export default class AppDetail extends Component {
  static displayName = 'App';
  constructor(props) {
    super(props);
    this.state = {
      redirect: null,
      app: null,
      minHeight:  Math.max(window.screen.availHeight*0.5, 400),
    };
  }
  componentDidMount() {
    if(this.props.app == null || this.props.app == undefined){
      const params = getParams(this.props.location.search);
      if(params.id == null || params.id == undefined){
        this.setState({
          redirect: '/app',
        })
      }else{
        queryApp(params.id).then((response) => {
          this.setState({
            app: response.data,
          })
        }).catch((error) => {
            alert(error);
        });
      }
    }else{
      this.setState({
        app: this.props.app,
      })
    }
  }
  componentWillUnmount() {
    // emitter.removeListener('show_attach', this.showAttach);
  }

  render() {
    if (this.state.redirect != null) {
      return (
        <Redirect to={{ pathname: this.state.redirect}} />
      );
    }
    if (this.state.app == null){
      return (
        <IceContainer>
          <Loading >
            <div>Loading......</div>
          </Loading>
        </IceContainer>
      )
    }
    return (
      <div>
        <IceContainer title={this.state.app.name} style={{minHeight: this.state.minHeight}}>
          <Tab>
            <Tab.Item title="Partition" key="1">
              <Partition app={this.state.app}/>
            </Tab.Item>
            <Tab.Item title="NewInfo" key="2">
              <IceContainer title="Add Information" >
                <AddTask app={this.state.app} type='InfoCollection' functionVisible={false}/>
              </IceContainer>

            </Tab.Item>
            <Tab.Item title="Infos" key="3">
              <Info app={this.state.app}></Info>
            </Tab.Item>
          </Tab>
        </IceContainer>
      </div>
    );
  }
}
