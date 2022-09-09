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

// required for single-page app
config.devServer.historyApiFallback = true
