module.exports = {
  lintOnSave: false,
  devServer: {
    port: 8081, // 端口
    proxy: {
      '/': {
        // 目标 API 地址
        target: 'http://localhost:8888/',
        // 如果要代理 websockets
        ws: false,
        // 将主机标头的原点更改为目标URL
        changeOrigin: true
      }
    }
  },
  publicPath: process.env.NODE_ENV === 'production'
    ? './'
    : '/'
}
