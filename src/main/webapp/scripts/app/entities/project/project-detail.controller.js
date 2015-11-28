'use strict';

angular.module('peyekApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, Contractor, User, Document) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('peyekApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
