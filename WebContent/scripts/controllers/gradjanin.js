'use strict';

angular.module('gradjanin', [
	'resource.gradjani',
	'ui.bootstrap'])

.controller('gradjaninCtrl', function (Gradjanin,$rootScope, $scope, $routeParams, $log, $location, $q, $http) {
	$scope.pogresan = false;

	if($routeParams.gradjaninId!='new'){
		var gradjaninId = $routeParams.gradjaninId;


		Gradjanin.get({'gradjaninId':gradjaninId}).$promise.then(function (data) {
			$scope.gradjanin = data;

		});
	}

	else{
		$scope.gradjanin = new Gradjanin();

	}
	//funkcija koja otvara datepicker
	$scope.openDatepicker = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope[opened] = true;
	};


	$scope.save = function () {



		if($scope.gradjanin.id){
			$log.info($scope.gradjanin);

			$scope.gradjanin.$update({gradjaninId:$scope.gradjanin.id},function () {
				$location.path('/gradjanin-list');
			});
		}
		else{
			$scope.gradjanin.$save(function () {
				$location.path('/gradjanin-list');
			});
		}
		$log.info("save");
	}

	$scope.delete = function () {
		if($scope.gradjanin.id){
			$scope.gradjanin.$delete({gradjaninId:$scope.gradjanin.id}, function () {
				$location.path('/gradjanin-list');
			});
		}
	}




	/*$scope.$watch( 'current',
		function(newValue, oldValue){
			console.log('firstName Changed');
			console.log(newValue);
			console.log(oldValue);
			$scope.triggerChangeWithApply();
		}
	);*/

	/*$scope.triggerChangeWithApply = function () {

		console.log('First name being reset');
		$scope.$apply(function(){
			$scope.current = 'lll';
			}
		);

	};*/

	$scope.login = function () {
		console.log($scope.gradjanin);
		var deferred = $q.defer();
			//user.password = md5.createHash(user.password);
		$http({
			url: "https://localhost:8443/xws/api/gradjanin/login",
			method: "POST",
			data: $scope.gradjanin
		}).then(function successCallback(data) {
			deferred.resolve(data.data);
		}, function errorCallback(response) {
			$scope.pogresan = true;
			}
			);

		var promise = deferred.promise;


		promise.then(function (data) {

			$rootScope.current.ime = data.ime;
			$rootScope.current.prezime = data.prezime;
			$rootScope.current.role = data.role;
			$rootScope.current.email = data.email;


			if(data.role === 'G'){
				$location.path('akt-list');
			}else if(data.role === 'O'){
				$location.path('akt-list');
			}else if(data.role ==='P'){
				$location.path('akt-list');
			}

		});
	}



});
