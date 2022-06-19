config.resolve.modules.push("../../processedResources/js/main");

if (config.devServer) {
  config.devServer.hot = true;
  config.devtool = 'eval-cheap-source-map';
  config.devServer.host = "0.0.0.0"
} else {
  config.devtool = undefined;
}

// disable bundle size warning
config.performance = {
  assetFilter: function (assetFilename) {
    return !assetFilename.endsWith('.js');
  },
};


// // dev server
// config.devServer = {
//   open: false,
//   // devMiddleware: {
//   //   index: false, // specify to enable root proxying
//   // },
//   port: 3000,
//   proxy: {
//     // context: () => true,
//     // target: "http://localhost:12080",
//     // secure: false,
//     // ws: true,
//     // changeOrigin: true,
//
//     // "/tiles": {
//     //   target: "http://localhost:12080",
//     //   // "pathRewrite": { "^/tiles": "" },
//     //   // "logger": "console",
//     //   // "logLevel": "debug",
//     //   secure: false,
//     //   changeOrigin: true,
//     // },
//     // "/ws": {
//     //   target: "http://localhost:12080",
//     //   // pathRewrite: { "^/ws": "" },
//     //   // "logLevel": "debug",
//     //   // "logger": "console",
//     //   secure: false,
//     //   ws: true,
//     //   changeOrigin: true,
//     // },
//   },
//   "static": [
//     // "../../processedResources/js/main",
//     "D:\\Users\\Adam\\Projects\\games\\kafkatorio\\modules\\web-map\\build\\processedResources\\frontend\\main",
//   ],
// };
