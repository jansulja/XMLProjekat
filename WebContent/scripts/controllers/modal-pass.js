'use strict';

angular.module('modal-pass',[])

.controller('modal-passCtrl', function ($scope, $modalInstance ,$q,$http,$rootScope) {

	$scope.password = "";

	$scope.cancel = function () {
		$modalInstance.close();
	};

	$scope.ok = function () {

		var deferred = $q.defer();
		//user.password = md5.createHash(user.password);
			$http({
				url: "https://localhost:8443/xws/api/akt/checkPass",
				method: "POST",
				data: $scope.password
			}).success(function (data) {
				deferred.resolve(data);
			});

			var promise = deferred.promise;


			promise.then(function (data) {

				$rootScope.dodajAkt();


			});

			$modalInstance.close();

	};



});
