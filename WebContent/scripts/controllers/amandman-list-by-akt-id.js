'use strict';

angular.module('amandman-list-by-akt-id',[])

.controller('amandman-list-by-akt-idCtrl', function ($scope, $q, $http, $location, $cacheFactory,$rootScope){

	var aktId = $rootScope.aktId;
	var deferred = $q.defer();
		$scope.amandmani = [];

		$http({
		  method: 'GET',
		  url: 'https://localhost:8443/xws/api/amandman/list/' + aktId,
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

		$scope.kojiNiz = function (akt){
		if(akt.status ==="PREDLOZEN"){
			$scope.names = ["PREDLOZEN","USVOJEN", "ODBIJEN"];
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
			$scope.names = ["USVOJEN"]
		}

		return $scope.names;
	}


});