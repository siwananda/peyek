 'use strict';

angular.module('peyekApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-peyekApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-peyekApp-params')});
                }
                return response;
            }
        };
    });
