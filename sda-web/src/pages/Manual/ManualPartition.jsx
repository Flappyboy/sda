import React, { Component } from 'react';
import ReactMarkdown from 'react-markdown/with-html';
// import Marked from 'marked';
import axios from 'axios';
import IceContainer from '@icedesign/container';


export default class ManualPartition extends Component {
  static displayName = 'Menu';

  constructor(props){
    super(props);
    this.state = {
      markdownText: ''
    }
  }
  componentDidMount() {

  }

  componentWillUnmount() {

  }
  componentDidMount() {
    axios
      .get("/doc/manual/partition.md")
      .then((response) => {
        console.log(response.data);
        this.setState({
          loading: false,
        });
        this.setState({
          markdownText: response.data,
        })
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
        <ReactMarkdown source={this.state.markdownText} />
      </IceContainer>
    );
  }
}
