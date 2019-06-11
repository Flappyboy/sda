import React, { PureComponent } from 'react';
import { Link } from 'react-router-dom';
import './index.scss';

export default class Logo extends PureComponent {
  render() {
    return (
      <div style={{marginLeft: 0}} className="logo">
        <Link to="/" className="logo-text">
          {/*<img style={{ filter: "grayscale(70%)",  opacity: 0.8,height: 60}} src="/favicon.png"/>*/}
          {/*<img style={{ filter: "grayscale(20%)", height: 80}} src="/favicon.png"/>*/}
          {/*<img style={{ filter: "invert(50%)", height: 80}} src="/favicon.png"/>*/}
          <img style={{ filter: "invert(30%)", height: 80}} src="/logo2_01.png"/>
          {/*<img style={{ filter: "grayscale(20%)", height: 60}} src="/logo.png"/>*/}
        </Link>
      </div>
    );
  }
}
