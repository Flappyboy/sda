// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置

const headerMenuConfig = [
  {
    name: 'feedback',
    path: 'https://github.com/alibaba/ice',
    external: true,
    newWindow: true,
    icon: 'message',
  },
  {
    name: 'help',
    path: 'https://alibaba.github.io/ice',
    external: true,
    newWindow: true,
    icon: 'bangzhu',
  },
];

const asideMenuConfig = [
  {
    name: '应用',
    path: '/app',
    icon: 'home',
  },
  {
    name: '测试',
    path: '/test',
    icon: 'home',
  },
  {
    name: '应用详细',
    path: '/appdetail',
    icon: 'home',
  },
  {
    name: '统计数据',
    path: '/statistics',
    icon: 'home',
  },
  {
    name: '划分结果',
    path: '/partition',
    icon: 'home',
  },
];

export { headerMenuConfig, asideMenuConfig };
