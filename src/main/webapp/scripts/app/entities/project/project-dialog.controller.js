'use strict';

angular.module('peyekApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Project', 'Contractor', 'User', 'Document',
        function($scope, $stateParams, $modalInstance, entity, Project, Contractor, User, Document) {

        $scope.project = entity;
        $scope.contractors = Contractor.query();
        $scope.users = User.query();
        $scope.documents = Document.query();
        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('peyekApp:projectUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveFinished);
            } else {
                Project.save($scope.project, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
