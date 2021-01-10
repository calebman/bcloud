const path = require('path');
const SwaggerOptionsParse = require('./parse/swagger-options-parse');
const SourceRender = require('./render/source-render');
const defaultOptions = {
  url: 'http://localhost:8888/bcloud/api-docs',
  target: path.join(__dirname, '../dist'),
  template: path.join(__dirname, './templates/api.ejs')
}

const swaggerOptionsParse = new SwaggerOptionsParse(defaultOptions);
const sourceRender = new SourceRender(defaultOptions);
swaggerOptionsParse.parse().then(options => {
  options.methods.forEach(methodOptions => {
    sourceRender.render(methodOptions);
  });
})

