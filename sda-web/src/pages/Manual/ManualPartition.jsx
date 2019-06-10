import React, { Component } from 'react';
import Marked from 'marked';
import axios from 'axios';
import IceContainer from '@icedesign/container';


export default class ManualPartition extends Component {
  static displayName = 'Menu';

  constructor(props){
    super(props);
    Marked.setOptions({
      renderer: new Marked.Renderer(),
      highlight: function(code) {
        return require('highlight.js').highlightAuto(code).value;
      },
      pedantic: false,
      gfm: true,
      tables: true,
      breaks: false,
      sanitize: false,
      smartLists: true,
      smartypants: false,
      xhtml: false
    });

    this.state = {
      markdownText: ''
    }
  }
  componentDidMount() {

  }

  componentWillUnmount() {

  }



  handleMark(){


  }


  //当组件输出到 DOM 后会执行 componentDidMount()
  componentDidMount() {
    axios
      .get("/doc/doc.md")
      .then((response) => {
        console.log(response.data);
        this.setState({
          loading: false,
        });
        document.getElementById('content').innerHTML =
          Marked(response.data);
      })
      .catch(function(error) {
        alert(error);
        this.setState({
          loading: false,
        });
      });
  }

  render() {
    return (
      <IceContainer>
        <div id="content">
        </div>
      </IceContainer>
    );
  }
}
