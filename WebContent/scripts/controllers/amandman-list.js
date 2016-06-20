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

		$scope.dalJePredsednik = function(){
		if($rootScope.current.role === 'P'){
			return false;
		}
		else{
		return true;
		}
	}

$scope.izmeniAmandman = function (amandman){

		var deferred = $q.defer();
		var zaPoslati = {id: amandman.id , status: amandman.status};

					$http({
					url: "https://localhost:8443/xws/api/amandman/update",
					method: "POST",
					data: zaPoslati,
					headers: { "Content-Type": 'application/json' }
				}).success(function (data) {
					deferred.resolve(data);
				});

				var promise = deferred.promise;
				promise.then(function (data) {


				});
	}


	$scope.kojiNiz = function (akt){
		if(akt.status ==="PREDLOZEN"){
			$scope.names = ["PREDLOZEN","USVOJEN_U_NACELU", "ODBIJEN"];
		}
		if(akt.status === "USVOJEN_U_NACELU"){
					$scope.names = ["USVOJEN_U_NACELU","USVOJEN_U_POJEDINOSTIMA", "ODBIJEN"];
		}
		if(akt.status === "USVOJEN_U_POJEDINOSTIMA" ){
			$scope.names = ["USVOJEN_U_POJEDINOSTIMA","USVOJEN_U_CELINI", "ODBIJEN"];
		}
				
		if(akt.status === "USVOJEN_U_CELINI"){
			$scope.names = ["USVOJEN_U_CELINI"];
		}

		if(akt.status === "ODBIJEN"){
			$scope.names = ["ODBIJEN"];
		}

		if(akt.status ==="USVOJEN"){
			$scope.names = ["USVOJEN_U_CELINI"]
		}

		return $scope.names;
	}


});