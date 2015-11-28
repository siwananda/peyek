'use strict';

angular.module('peyekApp')
    .controller('UserActivityController', function ($scope, UserActivity, ParseLinks) {
        $scope.userActivitys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            UserActivity.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userActivitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            UserActivity.get({id: id}, function(result) {
                $scope.userActivity = result;
                $('#deleteUserActivityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserActivity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserActivityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userActivity = {
                name: null,
                type: null,
                id: null
            };
        };
    });
