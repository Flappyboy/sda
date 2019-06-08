/**
 * 通过 mock api 的形式模拟实际项目中的接口
 * 可通过 mock/index.js 模拟数据，类似 express 的接口
 * 参考： https://alibaba.github.io/ice/docs/pro/mock
 */
import axios from 'axios';
import {IP, PORT, BASE} from './base.js';

export async function login(params) {
  return axios({
    url: '/api/login',
    method: 'post',
    data: params,
  });
}

export async function postUserRegister(params) {
  return axios({
    url: '/api/register',
    method: 'post',
    data: params,
  });
}

export async function postUserLogout() {
  return axios({
    url: '/api/logout',
    method: 'post',
  });
}

export async function getUserProfile() {
  return axios('/api/profile');
}

export default {
  login,
  postUserRegister,
  postUserLogout,
  getUserProfile,
};


const ip = IP;
// const ip = '172.19.240.73';
// const ip = '172.19.163.242';
// const port = '8029';
const port = PORT;
// const port = '8093';
// const base = '/mock/api';
const base = '/api';
const baseLocation = `${BASE}${base}`;

global.base = {
  ip,
  port,
  base,
  baseLocation,
};

const app = 'app';
const appBase = `${baseLocation}/${app}`;
export async function addApp(params) {
  return axios({
    url: appBase,
    method: 'post',
    data: params,
  });
}
export async function updateApp(params) {
  return axios({
    url: appBase,
    method: 'put',
    data: params,
  });
}
export async function queryAppList(inParams) {
  return axios({
    url: appBase,
    method: 'get',
    params: inParams,
  });
}
export async function queryApp(id) {
  return axios({
    url: `${appBase}/${id}`,
    method: 'get',
  });
}
export async function delApp(id) {
  return axios({
    url: `${appBase}/${id}`,
    method: 'delete',
  });
}
export async function delApps(params) {
  return axios({
    url: `${appBase}s`,
    method: 'delete',
    data: params,
  });
}

const statistics = 'dynaInfo';
const statisticsBase = `${baseLocation}/${statistics}`;
export async function addStatistics(params) {
  return axios({
    url: statisticsBase,
    method: 'post',
    data: params,
  });
}
export async function queryStatisticsList(inParams) {
  return axios({
    url: statisticsBase,
    method: 'get',
    params: inParams,
  });
}
export async function queryStatistics(id) {
  return axios({
    url: `${statisticsBase}/${id}`,
    method: 'get',
  });
}
export async function delStatistics(id) {
  return axios({
    url: `${statisticsBase}/${id}`,
    method: 'delete',
  });
}

const call = 'dynaCall';
const callBase = `${baseLocation}/${call}`;
export async function queryCallList(inParams) {
  return axios({
    url: callBase,
    method: 'get',
    params: inParams,
  });
}


const partition = 'partition';
const partitionBase = `${baseLocation}/${partition}`;
export async function addPartition(params) {
  return axios({
    url: partitionBase,
    method: 'post',
    data: params,
  });
}
export async function updatePartition(params) {
  return axios({
    url: partitionBase,
    method: 'put',
    data: params,
  });
}
export async function queryPartitionList(inParams) {
  // inParams.dynamicInfoId = 1;
  // inParams.algorithmsId = 1;
  // inParams.type = 1;
  return axios({
    url: partitionBase,
    method: 'get',
    params: inParams,
  });
}
export async function queryPartition(id) {
  return axios({
    url: `${partitionBase}/${id}`,
    method: 'get',
  });
}
export async function delPartition(id) {
  return axios({
    url: `${partitionBase}/${id}`,
    method: 'delete',
  });
}
export async function copyPartition(id) {
  return axios({
    url: `${partitionBase}/copy/${id}`,
    method: 'get',
  });
}
export async function evaluate(inParams){
  return axios({
    url: `${partitionBase}/evaluate/${inParams.id}`,
    method: 'get',
    params: inParams,
  });
}

export async function reRelation(data) {
  return axios({
    url: `${partitionBase}/re_relation`,
    method: 'post',
    data: data,
  });
}

const classN = 'class';
const classBase = `${baseLocation}/${classN}`;
export async function queryClassList(inParams) {
  return axios({
    url: classBase,
    method: 'get',
    params: inParams,
  });
}

const pD = `${baseLocation}/partition-detail`;
export async function queryPartitionDetail(id) {
  return axios.get(`${pD}/${id}`, {
    params: id,
  });
}

const node = 'partition-detail-node';
const nodeBase = `${baseLocation}/${node}`;
export async function queryNode(inParams) {
  return axios({
    url: `${nodeBase}/${inParams.id}`,
    method: 'get',
    params: inParams,
  });
}

const partitionResults = 'partition-results';
const partitionResultsBase = `${baseLocation}/${partitionResults}`;
export async function moveNode(inParams) {
  return axios({
    url: `${partitionResultsBase}/moveNode/${inParams.id}`,
    method: 'put',
    params: inParams,
  });
}
export async function moveNodes(inParams, nodes) {
  return axios({
    url: `${partitionResultsBase}/moveNode/${inParams.id}`,
    method: 'post',
    params: inParams,
    data: nodes,
  });
}
export async function addPartitionNode(inParams) {
  return axios({
    url: `${partitionResultsBase}`,
    method: 'post',
    data: inParams,
  });
}
export async function delPartitionNode(id) {
  return axios({
    url: `${partitionResultsBase}/${id}`,
    method: 'delete',
  });
}
export async function putPartitionNode(inParams) {
  return axios({
    url: `${partitionResultsBase}`,
    method: 'put',
    data: inParams,
  });
}

const edge = 'partition-detail-edge';
const edgeBase = `${baseLocation}/${edge}`;
export async function queryEdge(inParams) {
  return axios({
    url: `${edgeBase}/${inParams.id}`,
    method: 'get',
    params: inParams,
  });
}

const git = 'git';
const gitBase = `${baseLocation}/${git}`;
export async function addGit(params) {
  return axios({
    url: gitBase,
    method: 'post',
    data: params,
  });
}
export async function queryGitList(inParams) {
  return axios({
    url: gitBase,
    method: 'get',
    params: inParams,
  });
}

const partitionAlgorithm = 'algorithm/partition';
const partitionAlgorithmBase = `${baseLocation}/${partitionAlgorithm}`;
export async function partitionAlgorithms() {
  return axios({
    url: partitionAlgorithmBase,
    method: 'get',
  });
}

const functionService = 'function';
const functionServiceBase = `${baseLocation}/${functionService}`;
export async function queryFunctions(type) {
  return axios({
    url: `${functionServiceBase}/${type}`,
    method: 'get',
  });
}

const info = 'info';
const infoBase = `${baseLocation}/${info}`;
export async function queryInfos(params) {
  return axios({
    url: `${infoBase}`,
    method: 'get',
    params: params,
  });
}
export async function delInfos(name, data) {
  return axios({
    url: `${infoBase}`,
    method: 'delete',
    params: {name: name},
    data: data,
  });
}
export async function queryNodeInfo(params) {
  return axios({
    url: `${infoBase}/node`,
    method: 'get',
    params: params,
  });
}
export async function queryInfoTypes() {
  return axios({
    url: `${infoBase}/types`,
    method: 'get',
  });
}
export async function downloadInfoConfirm(params) {
  return axios({
    url: `${infoBase}/download_confirm`,
    method: 'get',
    params: params,
  });
}
export async function queryPairRelationsByAppId(appId) {
  return axios({
    url: `${infoBase}/pair_relations`,
    method: 'get',
    params: {appId: appId},
  });
}
export function downloadInfo(params) {
  window.open(`${infoBase}/download?id=${params.id}&name=${params.name}`);
}

const task = 'task';
const taskBase = `${baseLocation}/${task}`;
export async function doTask(params) {
  return axios({
    url: `${taskBase}/do`,
    method: 'post',
    data: params,
  });
}
export async function queryTaskById(id) {
  return axios({
    url: `${taskBase}/${id}`,
    method: 'get',
  });
}
export async function queryTasks(params) {
  return axios({
    url: `${taskBase}`,
    method: 'get',
    params:params,
  });
}

const evaluation = 'evaluation';
const evaluationBase = `${baseLocation}/${evaluation}`;
export async function evaluationLast(partitionId) {
  return axios({
    url: `${evaluationBase}/last`,
    method: 'get',
    params: {partitionId: partitionId},
  });
}
export async function evaluationLastForIds(params) {
  return axios({
    url: `${evaluationBase}/last`,
    method: 'post',
    data: params,
  });
}

export async function evaluationRedo(id) {
  return axios({
    url: `${evaluationBase}/redo`,
    method: 'get',
    params: {id: id},
  });
}
