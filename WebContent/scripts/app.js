'use strict';

/**
 * @ngdoc overview
 * @name invoiceClientApp
 * @description
 * # invoiceClientApp
 *
 * Main module of the application.
 */
 angular
 .module('invoiceClientApp', [
  'ngAnimate',
  'ngCookies',
  'ngResource',
  'ngRoute',
  'ngSanitize',
  'ngTouch',
  'angularCSS',
  'main',
  'about',
  'invoices',
  'invoice',
  'user',
  'resource.user',
  'gradjani',
  'gradjanin',
  'jcs-autoValidate',
  'akti',
  'akt-new',
  'akt-prikaz',
  'akt-list',
  'modal-pass',
  'amandman-new',
  'amandman-list',
  'amandman-list-by-akt-id'

  ])
 .config(function ($routeProvider) {
  $routeProvider
  .when('/', {
	  templateUrl: 'views/login.html',
    controller: 'gradjaninCtrl'
  })
  .when('/invoice-list', {
    templateUrl: 'views/invoice-list.html',
    controller: 'invoicesListCtrl'
  })
  .when('/invoice/:invoiceId', {
    templateUrl: 'views/invoice.html',
    controller: 'invoiceCtrl'
  })
  .when('/login', {
    templateUrl: 'views/login.html',
    controller: 'gradjaninCtrl'
  })
  .when('/gradjanin-list', {

	  templateUrl: 'views/gradjanin-list.html',
	  controller: 'gradjaninListCtrl'

  })
  .when('/gradjanin/:gradjaninId', {
    templateUrl: 'views/gradjanin.html',
    controller: 'gradjaninCtrl'
  })

  .when('/akt-list', {

	  templateUrl: 'views/akt-list.html',
	  controller: 'aktListCtrl'

  })
  .when('/akt/:aktId', {
	  templateUrl: 'views/akt.html',
	  controller: 'aktCtrl'
  })

  .when('/unauthorised', {
    templateUrl: 'views/unauthorised.html'
  })
  .when('/akt-new', {
    templateUrl: 'views/akt-new.html',
    controller: 'akt-newCtrl',
    css: ['bower_components/jquery.xmleditor/demo/stylesheets/reset.css','bower_components/jquery.xmleditor/demo/stylesheets/demo.css']
  })
  .when('/amandman-new', {
    templateUrl: 'views/amandman-new.html',
    controller: 'amandman-newCtrl',
    css: ['bower_components/jquery.xmleditor/demo/stylesheets/reset.css','bower_components/jquery.xmleditor/demo/stylesheets/demo.css']
  })
  .when('/akt-list', {
    templateUrl: 'views/akt-list.html',
    controller: 'akt-listCtrl'
  })
  .when('/akt-prikaz', {
	    templateUrl: 'views/akt-prikaz.html',
	    controller: 'akt-prikazCtrl',
	    css: 'styles/akt.css'
	  })
  .when('/amandman-list', {
      templateUrl: 'views/amandman-list.html',
      controller: 'amandman-listCtrl',
    })
  .when('/amandman-list-by-akt-id', {
      templateUrl: 'views/amandman-list.html',
      controller: 'amandman-list-by-akt-idCtrl',
    })
  
  .otherwise({
    redirectTo: '/'
  });
})
 //tricky deo
 .factory('authHttpResponseInterceptor',['$q','$location','$rootScope',function($q,$location,$rootScope){// fabrika koja pravi interceptor
  return {
    response: function(response){//ako smo dobili noramalan odgovor vratimo taj odgovor
      if (response.status === 401) {
        console.log("Response 401");
      }
      return response || $q.when(response);
    },
    responseError: function(rejection, x, y) {//ako smo dobili gresku

      if(rejection.status === 403){
        $location.path('/unauthorised');
      }


      if (rejection.status === 401 ) {//ako je greska 401 (korisnik nije prijavljen na sistem)
        console.log("Response Error 401",rejection);
        if($rootScope.current.ime == ""){

           $location.path('/login');
        }else{

          $location.path('/unauthorised');
        }

       //redirektujemo se na login
      }
      return $q.reject(rejection);//i odbacimo zahtev
    }
  }
}])
 .config(['$httpProvider',function($httpProvider) {//interceptor dodamo u stek interceptora
  $httpProvider.interceptors.push('authHttpResponseInterceptor');
}])
 //imamo problem: koristimo $resource koji je wrapper oko $http
 //kada pristigne odgovor prvo se uradi transformResponse nad odgovorom
 //pa se onda presretne odgovor.
 //uz gresku sa servera pristigne tekstualni opis greske
 //"Not logged in"
 //ovaj opis greske ne moze da se konvertuje u JSON (pogeldaj slajd $resource - napisano sitnim slovima)
 //zbog toga moramo da izmenimo transformResponce
 .run(['$http','$location','$rootScope',//to radimo u run, jer se izvrsava pre svega ostalog
  function($http, $location, $rootScope) {
    var parseResponse = function(response, headers, status) {//ova funkcija proba da konvertuje pristigli odgovor u JSON
      if(status===401){//ako je odgovor neautorizovan radimo redirekciju
        if($rootScope.current.ime == ""){

           $location.path('login');
        }else{

          $location.path('unauthorised');
        }
      }
      else{
        return response;//inace vratimo odgovor
      }
    };

    $http.defaults.transformResponse.unshift(parseResponse);//ovu funkciju stavimo na pocetak niza transformer funkcija
  }
  ])
 .controller('appCtrl', function($scope, User, $log, $location, $modal,$rootScope,$http){
  $scope.logout = function () {
      $http({
        url: "https://localhost:8443/xws/api/gradjanin/logout",
        method: "GET"
      }).success(function () {
        $rootScope.current = { ime: "", prezime: "", role: ""};
        $location.path("login");
      });
    }

  $scope.isLoginPage = function () {
    return $location.path() === '/login';
  };



  $rootScope.current = { ime: "", prezime: "", role: ""};
 /*var promise = CurrentUser.getCurrentUser();
   $scope.role = {};
   promise.then(function (data) {
     $log.info(data);
     $scope.role = data;
   });*/


  $scope.about = function (size) {
    var modalInstance = $modal.open({
      templateUrl: 'views/about.html',
      controller: 'AboutCtrl',
      size: size,
    });
  };






}).directive('head', ['$rootScope','$compile',
    function($rootScope, $compile){
        return {
            restrict: 'E',
            link: function(scope, elem){
                var html = '<link rel="stylesheet" ng-repeat="(routeCtrl, cssUrl) in routeStyles" ng-href="{{cssUrl}}" />';
                elem.append($compile(html)(scope));
                scope.routeStyles = {};
                $rootScope.$on('$routeChangeStart', function (e, next, current) {
                    if(current && current.$$route && current.$$route.css){
                        if(!angular.isArray(current.$$route.css)){
                            current.$$route.css = [current.$$route.css];
                        }
                        angular.forEach(current.$$route.css, function(sheet){
                            delete scope.routeStyles[sheet];
                        });
                    }
                    if(next && next.$$route && next.$$route.css){
                        if(!angular.isArray(next.$$route.css)){
                            next.$$route.css = [next.$$route.css];
                        }
                        angular.forEach(next.$$route.css, function(sheet){
                            scope.routeStyles[sheet] = sheet;
                        });
                    }
                });
            }
        };
    }
])


.run([
        'bootstrap3ElementModifier',
        function (bootstrap3ElementModifier) {
              bootstrap3ElementModifier.enableValidationStateIcons(true);
}])

 .run([
    'defaultErrorMessageResolver',
    function (defaultErrorMessageResolver) {
        // passing a culture into getErrorMessages('fr-fr') will get the culture specific messages
        // otherwise the current default culture is returned.
        defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
          errorMessages['requiredMy'] = 'Obavezno polje!';

        });
    }
]);
