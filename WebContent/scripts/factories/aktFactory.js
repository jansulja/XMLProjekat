angular.module('resource.akt', ['ngResource'])
	.factory('Akt', function ($resource) {
	return $resource('https://localhost:8443/xws/api/akt/:id',null, {
        'update': { method:'PUT' }
    });
})