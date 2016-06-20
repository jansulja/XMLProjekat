angular.module('resource.gradjani', ['ngResource'])
	.factory('Gradjanin', function ($resource,$log) {
		$log.info('INSERT at: ' + new Date());
	return $resource('https://localhost:8443/xws/api/gradjanin/:gradjaninId',null, {
        'update': { method:'PUT' }
    });
})