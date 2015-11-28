'use strict';

describe('Contractor Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockContractor, MockUser, MockDocument;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockContractor = jasmine.createSpy('MockContractor');
        MockUser = jasmine.createSpy('MockUser');
        MockDocument = jasmine.createSpy('MockDocument');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Contractor': MockContractor,
            'User': MockUser,
            'Document': MockDocument
        };
        createController = function() {
            $injector.get('$controller')("ContractorDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'peyekApp:contractorUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
