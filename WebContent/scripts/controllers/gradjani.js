'use strict';

angular.module('gradjani', ['resource.gradjani','angular-md5'])


 .controller('gradjaninListCtrl', function (Gradjanin, $scope, $location, md5, $log) {
 	
	$scope.gradjani = Gradjanin.query();
	$log.info($scope.gradjani.length);//0
	//kada smo kliknuli na red u tabeli prelazimo na stranicu za editovanje fakture sa zadatim id-om
 	
	$scope.insertOrEditGradjanin = function (gradjanin) {
 		if(gradjanin){
 			$location.path('/gradjanin/'+gradjanin.id);
 		}
 		else{
			$location.path('/gradjanin/new');
 		}
 	}
	
 });