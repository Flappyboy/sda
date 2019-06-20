import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import echarts from 'echarts/lib/echarts';
import 'echarts';
import { Dialog, Input, Button, Select, Checkbox, Form, NumberPicker, SplitButton, Table, Pagination, Grid } from '@alifd/next';
import { evaluationLastForIds } from "../../../../api";
import emitter from "../ev";

export default class EvaluationCompare extends Component {
  static displayName = 'EvaluationCompare';

  constructor(props) {
    super(props);
    this.state = {
      app: props.app,
      show: false,
    };
  }

  myChart = null;

  componentWillReceiveProps(nextProps) {

  }

  componentDidMount() {
    this.eventEmitter = emitter.addListener('evaluation_compare', this.compare.bind(this));
  }

  componentWillUnmount() {
    emitter.removeListener('evaluation_compare', this.compare.bind(this));
  }

  compare(partitionIds) {

    if(this.myChart == null){
      document.getElementById('evaluation').style.height = "600px";
      this.myChart = echarts.init(document.getElementById('evaluation'));
    }
    // 找到锚点
    const anchorElement = document.getElementById('evaluation');
    // 如果对应id的锚点存在，就跳转到锚点
    if (anchorElement) { anchorElement.scrollIntoView(); }

    this.myChart.showLoading();
    evaluationLastForIds(partitionIds).then((response)=>{
      this.loadData(response.data);
      this.setState({});
    }).catch((error) => {
      alert(error);
    });
  }

  loadData = (datas) => {
    datas = sortByCreatedAt(datas);

    const option = {
      legend: {
        data:['IRN','CHM','CHD','IFN', 'OPN']
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        },
        formatter: (params) => {
          let html = '<div>'+params[0].data.id+'</div>';
          const date = new Date(params[0].data.createdAt);
          html += '<div>'+date.getFullYear()+'/'+(date.getMonth()+1)+'/'+date.getDate()+' '
            +date.getHours()+':'+date.getMinutes()+':'+date.getSeconds()+'</div>';
          html += '<div>'+params[0].data.desc+'</div>';
          html += '<br/>';
          params.map((data) => {
            html += '<div>';
            html += '<div style="float:left;width:14px;height:14px;border-radius:7px;background-color:'+data.color+'"></div>';
            html += '<span style="margin-left:4px;">'+data.seriesName+':'+data.value[1]+'</span>';
            html += '</div>';
          });
          return html;
        },
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        min: 0,
        max: Math.max(3,datas.length-1),
        splitLine: {
          show: false
        },
      },
      yAxis: [
        {
          name: '',
          type: 'value',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#c04027'
            }
          },
        },
        {
          name: '',
          type: 'value',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#2664c0'
            }
          },
        },
      ],
      series: [
        {
          yAxisIndex: 0,
          name: 'IRN',
          data: formatData(datas, 'IRN'),
          type: 'line',
        },
        {
          yAxisIndex: 0,
          name: 'CHM',
          data: formatData(datas, 'CHM'),
          type: 'line'
        },
        // {
        //   yAxisIndex: 0,
        //   name: 'Instability',
        //   data: formatData(datas, 'Instability'),
        //   type: 'line'
        // },
        {
          yAxisIndex: 0,
          name: 'CHD',
          data: formatData(datas, 'CHD'),
          type: 'line'
        },
        {
          yAxisIndex: 0,
          name: 'IFN',
          data: formatData(datas, 'IFN'),
          type: 'line'
        },
        {
          yAxisIndex: 1,
          name: 'OPN',
          data: formatData(datas, 'OPN'),
          type: 'line'
        },
      ]
    };

    this.myChart.setOption(option);
    this.myChart.on('click', (params) => {
      emitter.emit('query_partition_detail', Object.assign({}, params.data.partition));
    });

    this.myChart.hideLoading();
  };


  render() {
      return (
          <div id="evaluation"/>
      );
  }
}

function sortByCreatedAt(datas) {
  return datas.sort((a, b) => {
    return a.createdAt - b.createdAt;
  });
}

function formatData(partitoins, indicatorName) {
  const datas = [];
  let index = 0;
  partitoins.map((p) => {
    let indicator = null;
    const e = p.lastEvaluation;
    if(e == null)
      return;

    for(let i in e.indicators){
      if(e.indicators[i].name === indicatorName){
        indicator = e.indicators[i];
        break;
      }
    }
    if(indicator == null){
      return;
    }
    const data = {
      partition: p,
      name: p.id,
      id: p.id,
      createdAt: p.createdAt,
      desc: p.desc,
      value: [index++, indicator.value],
    };
    datas.push(data);
  });
  return datas;
}
