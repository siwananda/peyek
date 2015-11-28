'use strict';

describe('UserActivity Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockUserActivity, MockUser, MockContractor;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockUserActivity = jasmine.createSpy('MockUserActivity');
        MockUser = jasmine.createSpy('MockUser');
        MockContractor = jasmine.createSpy('MockContractor');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'UserActivity': MockUserActivity,
            'User': MockUser,
            'Contractor': MockContractor
        };
        createController = function() {
            $injector.get('$controller')("UserActivityDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'peyekApp:userActivityUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
