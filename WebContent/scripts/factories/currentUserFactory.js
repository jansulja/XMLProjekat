angular.module('session', ['angular-md5'])
.factory('CurrentUser', function ($http, $q, $log, $location) {
	var service = {
		getCurrentUser : function  () {
			var deferred = $q.defer();
			//user.password = md5.createHash(user.password);
			$http({
				url: "https://localhost:8443/xws/api/gradjanin/current", 
				method: "GET"
			}).success(function (data) {
				deferred.resolve(data);
			});
			return deferred.promise;
		},
	};

	return service;
})