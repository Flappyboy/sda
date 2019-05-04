const webkit = require('./webkit-dep.json');
const partition1 = require('./partition1.json');
const partition2 = require('./partition2.json');
const partitionNode = require('./partition_node.json');
const partitionEdge = require('./partition_edge.json');
// const bar = require('./bar');

module.exports = {

  'GET /api/partition-detail': (req, res) => {
    const { id } = req.params;
    switch (id) {
      case '1':
        res.send(partition1);
        break;
      case '2':
        res.send(partition2);
        break;
      default:
        res.send(partition2);
    }
  },

  'GET /api/partition-detail-node': (req, res) => {
    res.send(partitionNode);
  },
  'GET /api/partition-detail-edge': (req, res) => {
    res.send(partitionEdge);
  },

  // 同时支持 GET 和 POST
  '/api/webkit': webkit,
  // '/api/foo/bar': bar(),

  // 支持标准 HTTP
  'GET /api/users': { users: [1, 2] },
  'DELETE /api/users': { users: [1, 2] },

  // 支持自定义函数，API 参考 express4
  'POST /api/users/create': (req, res) => {
    res.end('OK');
  },

  // 支持参数
  'POST /api/users/:id': (req, res) => {
    const { ida } = req.params;
    res.send({ id: ida });
  },
};
