angular.module('resource.glava', ['ngResource'])
	.factory('Glava', function ($resource) {
	return $resource('http://localhost:8089/xws/api/glava/:glavaId');
})