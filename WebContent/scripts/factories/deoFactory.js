angular.module('resource.deo', ['ngResource'])
	.factory('Deo', function ($resource) {
	return $resource('http://localhost:8089/xws/api/deo/:deoId');
})