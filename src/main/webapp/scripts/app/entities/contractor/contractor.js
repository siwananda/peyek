'use strict';

angular.module('peyekApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('contractor', {
                parent: 'entity',
                url: '/contractors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Contractors'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contractor/contractors.html',
                        controller: 'ContractorController'
                    }
                },
                resolve: {
                }
            })
            .state('contractor.detail', {
                parent: 'entity',
                url: '/contractor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Contractor'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contractor/contractor-detail.html',
                        controller: 'ContractorDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Contractor', function($stateParams, Contractor) {
                        return Contractor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('contractor.new', {
                parent: 'contractor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contractor/contractor-dialog.html',
                        controller: 'ContractorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('contractor', null, { reload: true });
                    }, function() {
                        $state.go('contractor');
                    })
                }]
            })
            .state('contractor.edit', {
                parent: 'contractor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contractor/contractor-dialog.html',
                        controller: 'ContractorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Contractor', function(Contractor) {
                                return Contractor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('contractor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
