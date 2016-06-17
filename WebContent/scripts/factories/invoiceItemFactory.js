angular.module('resource.invoiceItem', ['ngResource'])
	.factory('InvoiceItem', function ($resource) {
	return $resource('http://localhost:8089/xws/api/invoiceItem/:invoiceItemId');
})