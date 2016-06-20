'use strict';

angular.module('odeljak', [])

.controller('odeljakCtrl', function ($scope, $modalInstance, odeljak) {
	if(odeljak){
		$scope.odeljak = odeljak;
	}
	else{
		$scope.odeljak = {};	
	}
	$scope.ok = function () {
		$modalInstance.close({'odeljak':$scope.odeljak,
								'action':'save'});
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.delete = function () {
		$modalInstance.close({'odeljak':$scope.odeljak,
								'action':'delete'});
	};
});
