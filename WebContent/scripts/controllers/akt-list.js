'use strict';

angular.module('akt-list',[])

.controller('akt-listCtrl', function ($scope, $q, $http, $location, $cacheFactory,$rootScope){

		$scope.Upit = {Naziv: "", ID: "", Status: "" , DatumPredlogaOd: "" , DatumPredlogaDo: "", DatumUsvajanja : "" , Predlagac: "", Text: ""};
		$scope.names = ["USVOJEN", "ODBIJEN", "PREDLOZEN","U_PROCESU"];

		var deferred = $q.defer();
		$scope.akts = [];

		$http({
		  method: 'GET',
		  url: 'https://localhost:8443/xws/api/akt/list',
		  headers: { "Content-Type": 'application/json' }
		}).success(function (data) {
		    // this callback will be called asynchronously
		    // when the response is available
		    //$scope.akts.push(response.data);
			deferred.resolve(data);

		//  }, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.

		  });
		var promise = deferred.promise;
		promise.then(function (data) {

			$scope.akts = data;

		});


	//funkcija koja otvara datepicker
	$scope.openDatepicker1 = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope[opened] = true;
	};

	$scope.openDatepicker2 = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope[opened] = true;
	};

		$scope.openDatepicker3 = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope[opened] = true;
	};

$scope.searchAktove = function() {
			var deferred = $q.defer();

			$http({
				url: "https://localhost:8443/xws/api/akt/search",
				method: "POST",
				data: $scope.Upit

			}).success(function (data) {
				deferred.resolve(data);
			});

			var promise = deferred.promise;
			promise.then(function (data) {
				$scope.akts = data;
			});

	};



	$scope.obrisiAkt = function (akt){

		var deferred = $q.defer();

				$http({
				url: "https://localhost:8443/xws/api/akt/delete",
				method: "POST",
				data: akt
			}).success(function (data) {
				deferred.resolve(data);
			});

			var promise = deferred.promise;
			promise.then(function (data) {


			});

}


	$scope.show = function(akt,event){

//		var $httpDefaultCache = $cacheFactory.get('$http');
//		$httpDefaultCache.remove('/views/akt-prikaz.html');

		console.log(akt);
		console.log(event);

		if(event.target.tagName === 'TD'){


			var deferred = $q.defer();

			$http({
				  method: 'GET',
				  url: 'https://localhost:8443/xws/api/akt/' + akt.id,
				  headers: { "Content-Type": 'application/json' }
				}).success(function (data) {
				    // this callback will be called asynchronously
				    // when the response is available
				    //$scope.akts.push(response.data);
					deferred.resolve(data);

				//  }, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.

				  });
				var promise = deferred.promise;
				promise.then(function (data) {

					$rootScope.akt = data;
					$location.path('akt-prikaz');
					//$scope.akts = data;

				});


		}






	}


	$scope.delete = function(akt){

//		var $httpDefaultCache = $cacheFactory.get('$http');
//		$httpDefaultCache.remove('/views/akt-prikaz.html');

		$http({
			  method: 'DELETE',
			  url: 'https://localhost:8443/xws/api/akt/' + akt.id,
			  headers: { "Content-Type": 'application/json' }
			}).success(function (data) {
			    // this callback will be called asynchronously
			    // when the response is available
			    //$scope.akts.push(response.data);
				deferred.resolve(data);

			//  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.

			  });
			var promise = deferred.promise;
			promise.then(function (data) {




			});




	}

	$scope.predloziAmandman = function(akt){

		$rootScope.akt = akt;
		$location.path('amandman-new');


	}

	$scope.predloziAkt = function(){


		$location.path('akt-new');


	}

	$scope.pregledAmandmana = function(akt){


		$rootScope.aktId = akt.id;
		$location.path('amandman-list-by-akt-id');


	}



});