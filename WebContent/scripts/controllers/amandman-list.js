'use strict';

angular.module('amandman-list',[])

.controller('amandman-listCtrl', function ($scope, $q, $http, $location, $cacheFactory,$rootScope){


	var deferred = $q.defer();
		$scope.amandmani = [];

		$http({
		  method: 'GET',
		  url: 'https://localhost:8443/xws/api/amandman/list',
		  headers: { "Content-Type": 'application/json' }
		}).success(function (data) {

			deferred.resolve(data);

		}, function errorCallback(response) {


		});
		var promise = deferred.promise;
		promise.then(function (data) {

			$scope.amandmani = data;

		});


		$scope.isCurrent = function(amandman){


			if(amandman.odbornik === $rootScope.current.email) {
				return true;
			}else{
				return false;
			}




		}



});