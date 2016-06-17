'use strict';

angular.module('glava', [])

.controller('glavaCtrl', function ($scope, $modalInstance, glava) {
	if(glava){
		$scope.glava = glava;
	}
	else{
		$scope.glava = {};	
	}
	$scope.ok = function () {
		$modalInstance.close({'glava':$scope.glava,
								'action':'save'});
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.delete = function () {
		$modalInstance.close({'glava':$scope.glava,
								'action':'delete'});
	};
});
