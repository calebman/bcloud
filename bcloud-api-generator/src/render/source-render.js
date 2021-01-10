const fs = require('fs');
const path = require('path');
const ejs = require('ejs');
var beautify = require('js-beautify').js_beautify;
const _ = require('lodash');
const errHandler = err => {
  if (err) console.error(err);
}

module.exports = class SourceRender {
  constructor(options) {
    this.options = options;
  }

  render(methodOptions) {
    const targetPath = path.join(this.options.target, methodOptions.tag);
    this.createDirIfNotExist(targetPath);
    const templatePath = this.options.template;
    const renderTargetPath = path.join(targetPath, `${methodOptions.methodName}.js`);
    ejs.renderFile(templatePath, this.formatOptions(methodOptions), {}, (err, jsSource) => {
      if (err) {
        console.error(err);
      } else {
        jsSource = beautify(jsSource, { indent_size: 4, max_preserve_newlines: 2 });
        fs.writeFile(renderTargetPath, jsSource, errHandler);
      }
    });
  }

  formatOptions(methodOptions) {
    // resolve url prop for path param
    const pathParameters = methodOptions.parameters.filter(o => o.isPathParam)
    if (!_.isEmpty(pathParameters)) {
      _.forEach(pathParameters, param => {
        methodOptions.uri = methodOptions.uri.replace(`{${param.key}}`, '$' + `{params.${param.key}}`)
      })
    }
    // add extend props
    methodOptions.requestParamsDef = this.transferToDefStr(methodOptions.parameters)
    methodOptions.requestBodyDef = this.transferToDefStr(methodOptions.reqBodyProps)
    methodOptions.responseDef = this.transferToDefStr(methodOptions.respProps, obj => (obj.data || obj))
    methodOptions.hasRequestPrams = !_.isEmpty(methodOptions.parameters)
    methodOptions.hasRequestBody = !_.isEmpty(methodOptions.reqBodyProps)
    const respData = methodOptions.respProps.find(o => o.key === 'data')
    methodOptions.hasResponseData = !_.isEmpty(respData ? respData.children : methodOptions.respProps)
    return methodOptions;
  }

  transferToDefStr(params, format) {
    return JSON.stringify(this.transfer(params, format), null, 2)
  }

  transfer(params, format = obj => obj) {
    const resultObj = {};
    _.forEach(params, param => {
      if (_.isEmpty(param.children)) {
        if (_.isEmpty(param.options)) {
          resultObj[param.key] = `${param.title} @${param.type}`
        } else {
          resultObj[param.key] = `${param.title} #[${param.options.join(',')}]`
        }
      } else {
        if (param.type === 'array') {
          resultObj[param.key] = [this.transfer(param.children)];
        } else {
          resultObj[param.key] = this.transfer(param.children);
        }
      }
    })
    return format(resultObj);
  }

  createDirIfNotExist(dirname) {
    const mkdirsSync = (dirname) => {
      if (fs.existsSync(dirname)) {
        return true;
      } else {
        if (mkdirsSync(path.dirname(dirname))) {
          fs.mkdirSync(dirname);
          return true;
        }
      }
    }
    if (!fs.existsSync(dirname)) {
      mkdirsSync(dirname);
    }
  }
}