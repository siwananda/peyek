'use strict';

describe('Project Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProject, MockContractor, MockUser, MockDocument;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProject = jasmine.createSpy('MockProject');
        MockContractor = jasmine.createSpy('MockContractor');
        MockUser = jasmine.createSpy('MockUser');
        MockDocument = jasmine.createSpy('MockDocument');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Project': MockProject,
            'Contractor': MockContractor,
            'User': MockUser,
            'Document': MockDocument
        };
        createController = function() {
            $injector.get('$controller')("ProjectDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'peyekApp:projectUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
