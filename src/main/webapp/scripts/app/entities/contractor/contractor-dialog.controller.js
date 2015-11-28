'use strict';

angular.module('peyekApp').controller('ContractorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Contractor', 'User', 'Document',
        function($scope, $stateParams, $modalInstance, entity, Contractor, User, Document) {

        $scope.contractor = entity;
        $scope.users = User.query();
        $scope.documents = Document.query();
        $scope.load = function(id) {
            Contractor.get({id : id}, function(result) {
                $scope.contractor = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('peyekApp:contractorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.contractor.id != null) {
                Contractor.update($scope.contractor, onSaveFinished);
            } else {
                Contractor.save($scope.contractor, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
