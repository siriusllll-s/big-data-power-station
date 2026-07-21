module.exports = {
  runtimeCompiler: true,
  lintOnSave: false,
  productionSourceMap: false,
  chainWebpack (config) {
    config.plugins.delete('prefetch')
  },
  pwa: {
    iconPaths: {
      favicon32: 'favicon.ico',
      favicon16: 'favicon.ico',
      appleTouchIcon: 'favicon.ico',
      maskIcon: 'favicon.ico',
      msTileImage: 'favicon.ico'
    }
  },
  devServer: {
    host: '0.0.0.0',
    port: 8080,
    // 允许通过 IP / 主机名访问，避免 Invalid Host header
    disableHostCheck: true,
    proxy: {
      // 登录/注册走云服务器
      '/api/login': {
        target: process.env.VUE_APP_AUTH_API || 'http://101.43.206.109:8787',
        pathRewrite: { '^/api': '' },
        changeOrigin: true,
        ws: false
      },
      '/api/register': {
        target: process.env.VUE_APP_AUTH_API || 'http://101.43.206.109:8787',
        pathRewrite: { '^/api': '' },
        changeOrigin: true,
        ws: false
      },
      // 其它业务接口走集群本地后端
      '/api': {
        target: process.env.VUE_APP_BASE_API || 'http://localhost:8113',
        pathRewrite: { '^/api': '/' },
        changeOrigin: true,
        ws: false
      }
    }
  }
}
