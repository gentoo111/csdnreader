module.exports = {
  lintOnSave: false,
  devServer: {
    port: 8081, // 端口
    proxy: {
      '/': {
        // 后端API地址
        target: 'http://localhost:8888/',
        // 代理 websockets
        ws: true,
        // 跨域
        changeOrigin: true
      }
    }
  },
  publicPath: process.env.NODE_ENV === 'production'
    ? './'
    : '/'
}
