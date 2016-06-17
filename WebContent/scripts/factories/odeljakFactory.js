angular.module('resource.odeljak', ['ngResource'])
	.factory('Odeljak', function ($resource) {
	return $resource('http://localhost:8089/xws/api/odeljak/:odeljakId');
})