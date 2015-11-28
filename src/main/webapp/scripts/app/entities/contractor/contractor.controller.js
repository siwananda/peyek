'use strict';

angular.module('peyekApp')
    .controller('ContractorController', function ($scope, Contractor, ParseLinks) {
        $scope.contractors = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Contractor.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.contractors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Contractor.get({id: id}, function(result) {
                $scope.contractor = result;
                $('#deleteContractorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Contractor.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteContractorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.contractor = {
                name: null,
                id: null
            };
        };
    });
