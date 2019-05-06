/**
 * 通过 mock api 的形式模拟实际项目中的接口
 * 可通过 mock/index.js 模拟数据，类似 express 的接口
 * 参考： https://alibaba.github.io/ice/docs/pro/mock
 */
import axios from 'axios';
import Qs from 'qs';

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


const ip = 'localhost';
// const ip = '172.19.240.73';
// const ip = '172.19.163.242';
// const port = '8029';
// const port = '8019';
const port = '8093';
// const base = '/mock/api';
const base = '/api';
// const baseLocation = `http://${ip}:${port}${base}`;
const baseLocation = `${base}`;

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
    url: `${partitionResultsBase}/${inParams.id}`,
    method: 'put',
    params: inParams,
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
    method: 'get'
  });
}

