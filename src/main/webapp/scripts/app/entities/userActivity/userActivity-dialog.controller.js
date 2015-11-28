'use strict';

angular.module('peyekApp').controller('UserActivityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserActivity', 'User', 'Contractor',
        function($scope, $stateParams, $modalInstance, entity, UserActivity, User, Contractor) {

        $scope.userActivity = entity;
        $scope.users = User.query();
        $scope.contractors = Contractor.query();
        $scope.load = function(id) {
            UserActivity.get({id : id}, function(result) {
                $scope.userActivity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('peyekApp:userActivityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.userActivity.id != null) {
                UserActivity.update($scope.userActivity, onSaveFinished);
            } else {
                UserActivity.save($scope.userActivity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
