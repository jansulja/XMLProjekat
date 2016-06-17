'use strict';

angular.module('akt-new',[])

.controller('akt-newCtrl', function($scope,$q,$http,$modal,$rootScope,$location){


		$scope.pass = function (size) {
		    var modalInstance = $modal.open({
		      templateUrl: 'views/modal-pass.html',
		      controller: 'modal-passCtrl',
		      size: size
		    });
		};

		$rootScope.dodajAkt = function(){

			$scope.xmlString = $($scope.root).xmlEditor("xml2Str");

			var deferred = $q.defer();

			$http({
				url: "https://localhost:8443/xws/api/akt/new",
				method: "POST",
				data: $scope.xmlString,
				headers: { "Content-Type": 'application/xml' }
			}).success(function (data) {
				deferred.resolve(data);
			});

			var promise = deferred.promise;
			promise.then(function (data) {
				console.log('success');
				$location.path('akt-list');
			});



		}


})

.directive("rootElementAkt", function() {
    return {
        link: function(scope, elem, attrs) {

        		var extractor = new Xsd2Json("akt.xsd", {"schemaURI":"schema/", "rootElement": "akt"});

				$(elem).xmlEditor({
					schema : extractor.getSchema(),
					enforceOccurs: true

				});

				scope.root = elem;







        }
    };
});

