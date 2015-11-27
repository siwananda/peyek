 'use strict';

angular.module('tenderguruApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-tenderguruApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-tenderguruApp-params')});
                }
                return response;
            }
        };
    });
