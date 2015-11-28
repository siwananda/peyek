'use strict';

angular.module('peyekApp')
    .factory('UserActivity', function ($resource, DateUtils) {
        return $resource('api/userActivitys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
