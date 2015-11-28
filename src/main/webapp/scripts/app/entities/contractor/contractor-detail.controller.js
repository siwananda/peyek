'use strict';

angular.module('peyekApp')
    .controller('ContractorDetailController', function ($scope, $rootScope, $stateParams, entity, Contractor, User, Document) {
        $scope.contractor = entity;
        $scope.load = function (id) {
            Contractor.get({id: id}, function(result) {
                $scope.contractor = result;
            });
        };
        var unsubscribe = $rootScope.$on('peyekApp:contractorUpdate', function(event, result) {
            $scope.contractor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
