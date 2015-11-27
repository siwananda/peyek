'use strict';

angular.module('tenderguruApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


