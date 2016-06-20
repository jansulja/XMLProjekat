'use strict';


angular.module('akt-prikaz',[])

.controller('akt-prikazCtrl', function ($scope,$rootScope) {


	$scope.akt = $rootScope.akt
	//$state.go($state.current, {}, {reload: true});

});