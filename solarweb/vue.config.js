module.exports = {
    runtimeCompiler: true,
    lintOnSave: process.env.NODE_ENV !== "production",
    lintOnSave: false,
    productionSourceMap:false,
    chainWebpack(config) {
        config.plugins.delete("prefetch")
    },
    pwa: {
        iconPaths: {
          favicon32: "favicon.ico",
          favicon16: "favicon.ico",
          appleTouchIcon: "favicon.ico",
          maskIcon: "favicon.ico",
          msTileImage: "favicon.ico"
        }
    },
    devServer: {
        host: "0.0.0.0",
        port: 8080,
        proxy: {
            "/api": {
                target: process.env.VUE_APP_BASE_API,
                pathRewrite:{"^/api":"/"},
                changeOrigin: true,
                ws: false
            },
        }
    }
}
