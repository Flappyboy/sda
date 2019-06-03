import React, { Component } from 'react';

import echarts from 'echarts/lib/echarts';
import 'echarts';
import emitter from '../ev';

const graphStyle = {
  height: 700,
};

const defaultColor = '#c71919';
const selectedColor = '#5370c7';

const calculateNodeSize = (size, sizeMin, sizeMax, sizeRangeMin, sizeRangeMax) => {
  return (sizeRangeMax * (size - sizeMin) + sizeRangeMin * (sizeMax - size)) / (sizeMax - sizeMin);
}

const transformNode = (node, sizeMin, sizeMax, sizeRangeMin, sizeRangeMax) => {
  return {
    id: node.id,
    name: node.name,
    desc: node.desc,
    data: node,
    symbolSize: calculateNodeSize(node.size, sizeMin, sizeMax, sizeRangeMin, sizeRangeMax),
    x: null,
    y: null,
    itemStyle: {
      normal: {
        color: defaultColor
      }
    }
    // draggable: true,
  };
}

const transformEdge = (edge) => {
  return {
    id: edge.id,
    source: edge.source,
    target: edge.target,
    data: edge,
    value: edge.count,
    symbolSize: [2, 0],
    label: {
      show: true,
    },
  };
}


class Graph extends Component {
  constructor(props) {
    super(props);
    this.state = {

    };
  }
  id = null;

  myChart = null;
  nodes = [];
  edges = [];

  sizeMax = 0;
  sizeMin = 9999;
  sizeRangeMin = 5;
  sizeRangeMax = 30;


  componentDidUpdate(prevProps) {
    if (this.props.isLoading) {
      this.myChart.showLoading();
    } else {
      this.myChart.hideLoading();

      this.props.data.nodes.forEach(element => {
        if (element.size > this.sizeMax) {
          this.sizeMax = element.size;
        }
        if (element.size < this.sizeMin) {
          this.sizeMin = element.size;
        }
      });
      this.nodes = this.props.data.nodes.map((node, idx) => {
        return transformNode(node, this.sizeMin, this.sizeMax, this.sizeRangeMin, this.sizeRangeMax);
      });
      this.edges = this.props.data.links.map((link, idx) => {
        return transformEdge(link);
      });
      if(this.props.data.id != this.id || this.myChart == null){
        this.id = this.props.data.id;
        this.loadData(this.props.data);
      }else{
        this.refreshData();
      }
    }
  }
  componentDidMount() {
    this.eventEmitter = emitter.addListener('query_partition_detail_ne', this.selectNode.bind(this));
    this.eventEmitter = emitter.addListener('partition_detail_operate', this.operateGraph);
    this.myChart = echarts.init(document.getElementById('graph'));
    this.myChart.showLoading();
  }

  // 组件销毁前移除事件监听
  componentWillUnmount() {
    emitter.removeListener('partition_detail_operate', this.operateGraph);
    emitter.removeListener('query_partition_detail_ne', this.selectNode.bind(this));
  }

  refreshData = () => {
    this.myChart.setOption({
      series: [{
        data: this.nodes,
        edges: this.edges,
      }]
    });
  };

  selectNode(type, node) {
    if(type == 'node'){
      this.operateGraph({selectNode: node});
    }
  }

  operateGraph = (operates) => {
    if(operates.reload){
      emitter.emit('query_partition_detail', null, true);
      return;
    }

    if(operates.deleteNodes) {
      for (let i in operates.deleteNodes) {
        const targetNode = operates.deleteNodes[i];
        for (let j in this.nodes) {
          const node = this.nodes[j];
          if (node.id === targetNode.id) {
            this.nodes.splice(j, 1);
            break;
          }
        }
      }
    }

    if(operates.addNodes) {
      for (let index in operates.addNodes) {
        const targetNode = operates.addNodes[index];
        this.nodes.unshift(transformNode(targetNode, this.sizeMin, this.sizeMax, this.sizeRangeMin, this.sizeRangeMax));
      }
    }

    if(operates.putNodes) {
      for (let i in operates.putNodes) {
        const sizeChange = operates.putNodes[i].sizeChange;
        const name = operates.putNodes[i].name;
        const desc = operates.putNodes[i].desc;
        for (let j in this.nodes) {
          const node = this.nodes[j];
          if (node.id === operates.putNodes[i].id) {
            if(sizeChange != undefined && sizeChange != null){
              node.data.size += sizeChange;
              node.symbolSize = calculateNodeSize(node.data.size);
            }
            if(name != undefined && name != null){
              node.data.name = name;
              node.name = name;
            }
            if(desc != undefined && desc != null){
              node.data.desc = desc;
              node.desc = desc;
            }
            break;
          }
        }
      }
    }

    if(operates.deleteEdges) {
      for (let index in operates.deleteEdges) {
        const targetEdge = operates.deleteEdges[index];
        for (let index in this.edges) {
          const edge = this.edges[index];
          if (edge.id === targetEdge.id) {
            this.edges.splice(index, 1);
            break;
          }
        }
      }
    }

    if(operates.addEdges) {
      for (let index in operates.addEdges) {
        const targetEdge = operates.addEdges[index];
        this.edges.push(transformEdge(targetEdge));
      }
    }

    if(operates.putEdges) {
      for (let key in operates.putEdges) {
        const value = operates.putEdges[key].count;
        for (let index in this.edges) {
          const edge = this.edges[index];
          if (edge.id === key) {
            edge.value += value;
          }
        }
      }
    }

    if(operates.selectNode) {
      for (let j in this.nodes) {
        const node = this.nodes[j];
        if (node.data.id == operates.selectNode.id) {
          node.itemStyle= { normal: { color: selectedColor } };
        }else{
          node.itemStyle= { normal: { color: defaultColor } };
        }
      }
    }
    this.refreshData();
  };

  loadData = (json) => {
    // 基于准备好的dom，初始化echarts实例
    // 绘制图表
    this.myChart.hideLoading();

    const option = {
      animation: false,
      series: [{
        type: 'graph',
        layout: 'force',
        label: {
          normal: {
            formatter: '{b}',
          },
        },
        roam: true,
        data: this.nodes,
        focusNodeAdjacency: true,
        categories: json.categories,
        force: {
          // initLayout: 'circular'
          // repulsion: 20,
          // edgeLength: [10, 100],
          repulsion: 200,
          gravity: 0.2,
          // layoutAnimation: false,
        },
        lineStyle: {
          normal: {
            curveness: 0.1,
          },
        },
        edgeSymbol: ['circle', 'arrow'],
        edgeLabel: {
          formatter: '{c}',
        },
        edges: this.edges,
      }],
    };

    this.myChart.setOption(option);
    this.myChart.on('click', (params) => {
      if (params.seriesType === 'graph') {
        if (params.dataType === 'edge') {
          // 点击到了 graph 的 edge（边）上。
          emitter.emit('query_partition_detail_ne', 'edge', params.data.data);
        } else {
          // 点击到了 graph 的 node（节点）上。
          emitter.emit('query_partition_detail_ne', 'node', params.data.data);
        }
      }
    });
  }

  render() {
    return (
      <div id="graph" style={graphStyle} />
    );
  }
}

export default Graph;
