// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称
import React from 'react';
import { getRouterData } from './utils/utils';
import { asideMenuConfig } from './menuConfig';

const UserLogin = React.lazy(() => import('./pages/UserLogin'));
const UserRegister = React.lazy(() => import('./pages/UserRegister'));
const Dashboard = React.lazy(() => import('./pages/Dashboard'));
const Terms = React.lazy(() => import('./pages/Terms'));
const Result = React.lazy(() => import('./pages/Result'));
const ProjectList = React.lazy(() => import('./pages/ProjectList'));
const Profile = React.lazy(() => import('./pages/Profile'));
const Setting = React.lazy(() => import('./pages/Setting'));
const Fail = React.lazy(() => import('./pages/Fail'));
const Empty = React.lazy(() => import('./pages/Exception/Empty'));
const Forbidden = React.lazy(() => import('./pages/Exception/Forbidden'));
const NotFound = React.lazy(() => import('./pages/Exception/NotFound'));
const ServerError = React.lazy(() => import('./pages/Exception/ServerError'));
const Statistics = React.lazy(() => import('./pages/Statistics'));
const Partition = React.lazy(() => import('./pages/Partition'));
const App = React.lazy(() => import('./pages/App'));
const AppDetailPage = React.lazy(() => import('./pages/AppDetailPage'));
const Task = React.lazy(() => import('./pages/Task'));
const Test = React.lazy(() => import('./pages/Test'));
const ManualPartition = React.lazy(() => import('./pages/Manual/ManualPartition'));
const ManualDemo = React.lazy(() => import('./pages/Manual/ManualDemo'));
const ManualEvaluation = React.lazy(() => import('./pages/Manual/ManualEvaluation'));
const ManualInfo = React.lazy(() => import('./pages/Manual/ManualInfo'));
const ManualOther = React.lazy(() => import('./pages/Manual/ManualOther'));



const routerConfig = [
  {
    path: '/manual/manual_partition',
    component: ManualPartition,
  },
  {
    path: '/manual/manual_demo',
    component: ManualDemo,
  },
  {
    path: '/manual/manual_evaluation',
    component: ManualEvaluation,
  },
  {
    path: '/manual/manual_info',
    component: ManualInfo,
  },
  {
    path: '/manual/manual_other',
    component: ManualOther,
  },
  {
    path: '/test',
    component: Test,
  },
  {
    path: '/partition',
    component: Partition,
  },
  {
    path: '/statistics',
    component: Statistics,
  },
  {
    path: '/app',
    component: App,
  },
  {
    path: '/appDetail',
    component: AppDetailPage,
  },
  {
    path: '/task',
    component: Task,
  },
  {
    path: '/dashboard/monitor',
    component: Dashboard,
  },
  {
    path: '/list/general',
    component: ProjectList,
  },
  {
    path: '/result/success',
    component: Result,
  },
  {
    path: '/result/fail',
    component: Fail,
  },
  {
    path: '/profile/basic',
    component: Profile,
  },
  {
    path: '/profile/general',
    component: Terms,
  },
  {
    path: '/account/setting',
    component: Setting,
  },
  {
    path: '/exception/500',
    component: ServerError,
  },
  {
    path: '/exception/403',
    component: Forbidden,
  },
  {
    path: '/exception/204',
    component: Empty,
  },
  {
    path: '/exception/404',
    component: NotFound,
  },
  {
    path: '/user/login',
    component: UserLogin,
  },
  {
    path: '/user/register',
    component: UserRegister,
  },
];

const routerData = getRouterData(routerConfig, asideMenuConfig);

export { routerData };
