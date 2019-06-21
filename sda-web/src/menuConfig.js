// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置

const headerMenuConfig = [
  // {
  //   name: 'feedback',
  //   path: 'https://github.com/Flappyboy/sda',
  //   external: true,
  //   newWindow: true,
  //   icon: 'message',
  // },
  {
    name: 'help',
    path: 'https://github.com/Flappyboy/sda',
    external: true,
    newWindow: true,
    icon: 'bangzhu',
  },
];

const asideMenuConfig = [
  {
    name: '应用',
    path: '/app',
    icon: 'home2',
  },
  {
    name: '任务',
    path: '/task',
    icon: 'result',
  },
  /*{
    name: '测试',
    path: '/test',
    icon: 'home',
  },*/
  {
    name: 'Manual',
    path: '/manual',
    children: [
      {
        name: 'ManualDemo',
        path: '/manual/manual_demo',
      },
      {
        name: 'ManualPartition',
        path: '/manual/manual_partition',
      },
      {
        name: 'ManualInfo',
        path: '/manual/manual_info',
      },
      {
        name: 'ManualEvaluation',
        path: '/manual/manual_evaluation',
      },
      {
        name: 'ManualOther',
        path: '/manual/manual_other',
      },
    ],
  },
];

export { headerMenuConfig, asideMenuConfig };
