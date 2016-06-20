angular.module('resource.invoice', ['ngResource'])
	.factory('Invoice', function ($resource) {
	return $resource('http://localhost:8089/xws/api/invoice/:invoiceId',null, {
        'update': { method:'PUT' }
    });
})