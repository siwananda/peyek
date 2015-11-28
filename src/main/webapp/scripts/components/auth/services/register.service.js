'use strict';

angular.module('peyekApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


