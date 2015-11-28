'use strict';

angular.module('peyekApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userActivity', {
                parent: 'entity',
                url: '/userActivitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserActivitys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userActivity/userActivitys.html',
                        controller: 'UserActivityController'
                    }
                },
                resolve: {
                }
            })
            .state('userActivity.detail', {
                parent: 'entity',
                url: '/userActivity/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserActivity'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userActivity/userActivity-detail.html',
                        controller: 'UserActivityDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserActivity', function($stateParams, UserActivity) {
                        return UserActivity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userActivity.new', {
                parent: 'userActivity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userActivity/userActivity-dialog.html',
                        controller: 'UserActivityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userActivity', null, { reload: true });
                    }, function() {
                        $state.go('userActivity');
                    })
                }]
            })
            .state('userActivity.edit', {
                parent: 'userActivity',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userActivity/userActivity-dialog.html',
                        controller: 'UserActivityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserActivity', function(UserActivity) {
                                return UserActivity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userActivity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
