'use strict';

angular.module('peyekApp')
    .controller('UserActivityDetailController', function ($scope, $rootScope, $stateParams, entity, UserActivity, User, Contractor) {
        $scope.userActivity = entity;
        $scope.load = function (id) {
            UserActivity.get({id: id}, function(result) {
                $scope.userActivity = result;
            });
        };
        var unsubscribe = $rootScope.$on('peyekApp:userActivityUpdate', function(event, result) {
            $scope.userActivity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
