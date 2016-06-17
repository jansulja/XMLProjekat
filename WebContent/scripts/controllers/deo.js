'use strict';

angular.module('deo', [])

.controller('deoCtrl', function ($scope, $modalInstance, deo) {
	if(deo){
		$scope.deo = deo;
	}
	else{
		$scope.deo = {};	
	}
	$scope.ok = function () {
		$modalInstance.close({'deo':$scope.deo,
								'action':'save'});
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.delete = function () {
		$modalInstance.close({'deo':$scope.deo,
								'action':'delete'});
	};
});
