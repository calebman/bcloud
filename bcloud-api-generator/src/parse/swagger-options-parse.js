'use strict';
const swagger = require('swagger-client')
const _ = require('lodash');


module.exports = class SwaggerOptionsParse {
  constructor(options) {
    this.options = options;
  }

  parse () {
    return new Promise((resolve, reject) => {
      swagger({
        url: this.options.url
      }).then(res => {
        resolve(this.parseJsonObj(res.spec));
      }).catch(err => reject(err))
    });
  }

  parseJsonObj (swaggerJsonObj) {
    var methods = [];
    var data = {
      moduleName: swaggerJsonObj.moduleName,
      methods: []
    };
    // paths apis
    _.forEach(swaggerJsonObj.paths, (path, uri) => {
      // api methods
      var parameters = [];
      _.forEach(path, (api, methodName) => {
        var method = {
          uri: uri,
          methodName: api.operationId,
          httpMethod: methodName.toUpperCase(),
          title: api.summary,
          parameters: [],
          reqBodyProps: [],
          respProps: [],
          tag: _.isEmpty(api.tags) ? 'default' : api.tags[0]
        }
        // load parameters
        if (api.parameters && api.parameters.length > 0) {
          api.parameters.forEach((param) => {
            if (_.some(parameters, { key: param.name })) {
              return
            }
            // load options
            let options = []
            if (param.enum) {
              options = param.enum
            }
            var parameter = {
              key: param.name,
              type: _.get(param, 'schema.type'),
              title: param.description,
              isPathParam: param.in === 'path',
              options,
              pattern: param.pattern,
              required: param.required
            }
            parameters.push(parameter);
          })
          method.parameters = parameters;
        }
        // load body data
        if (api.requestBody) {
          const schema = _.get(api.requestBody, 'content.application/json.schema')
          if (schema) {
            method.reqBodyProps = this.loadProps(schema.properties, schema.required);
          }
        }
        // load resp data
        if (!_.isEmpty(api.responses)) {
          const schema = _.get(api.responses, '200.content.*/*.schema');
          if (schema) {
            method.respProps = this.loadProps(schema.properties, schema.required);
          }
        }
        methods.push(method);
      })
    });
    data.methods = methods;
    return data;
  };

  loadProps (props, requiredProps = []) {
    var properties = [];
    if (!_.isEmpty(props)) {
      _.forEach(props, (prop, key) => {
        if (_.some(properties, { key })) {
          return
        }
        requiredProps = _.isEmpty(prop.required) ? requiredProps : prop.required;
        // load options
        let options = []
        if (prop.enum) {
          options = prop.enum
        }
        // load children
        let children = []
        switch (prop.type) {
          case 'object':
            children = prop.properties
            break;
          case 'array':
            children = _.get(prop, 'items.properties')
            break;
        }
        var property = {
          key: key,
          type: prop.type,
          title: prop.title,
          options,
          pattern: prop.pattern,
          required: _.includes(requiredProps, key),
          children: this.loadProps(children, requiredProps)
        }
        properties.push(property);
      })
    }
    return properties;
  }
}