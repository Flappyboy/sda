// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import { getRouterData } from './utils/utils';
import { asideMenuConfig } from './menuConfig';

import UserLogin from './pages/UserLogin';
import UserRegister from './pages/UserRegister';
import Terms from './pages/Terms';
import Result from './pages/Result';
import CardList from './pages/CardList';
import GeneralTable from './pages/GeneralTable';
import Profile from './pages/Profile';
import Setting from './pages/Setting';
import NotFound from './pages/NotFound';
import Fail from './pages/Fail';
import ServerError from './pages/ServerError';
import Statistics from './pages/Statistics';
import Partition from './pages/Partition';
import BasicLayout from './layouts/BasicLayout';
import Forbidden from './pages/Forbidden';
import App from './pages/App';

const routerConfig = [
  {
    path: '/list/card',
    component: CardList,
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
    path: '/portlets/terms',
    component: Terms,
  },
  {
    path: '/table/general',
    component: GeneralTable,
  },
  {
    path: '/account/profile',
    component: Profile,
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
  {
    path: '/partition',
    layout: BasicLayout,
    component: Partition,
  },
  {
    path: '/statistics',
    layout: BasicLayout,
    component: Statistics,
  },
  {
    path: '/app',
    layout: BasicLayout,
    component: App,
  },
];

const routerData = getRouterData(routerConfig, asideMenuConfig);

export { routerData };
