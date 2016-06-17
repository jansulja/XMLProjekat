'use strict';

angular.module('akt', [
	'resource.akt',
	'deo',
	'resource.deo',
	'glava',
	'resource.glava',
	'odeljak',
	'resource.odeljak',
	'ui.bootstrap'])

.controller('aktCtrl', function (Akt,$rootScope, $scope, $routeParams, $log, $location, $q, $http, $modal) {




	//---------------Osnovno nesto---------------------------------------------------------
	if($routeParams.aktId!='new'){
		var aktId = $routeParams.aktId;


		Akt.get({'aktId':aktId}).$promise.then(function (data) {
			$scope.akt = data;

		});
	}

	else{
		$scope.akt = new Akt();


		$scope.akt.Uvodni_deo = {};
		$scope.akt.Uvodni_deo.Definicija = [];


	}

	//-------------------------------------------------------------------------------------------------
$scope.hideDeoTabela = function () {
    if($scope.akt.delovi.length === 0)

    	return true;
    else
    	return false;
};

$scope.hideOdeljak = function () {
    if($scope.akt.glave.length === 0)

    	return true;
    else
    	return false;
};

$scope.hideRetry = function () {
    if($scope.akt.delovi.length === 0)

    	return true;
    else
    	return false;
};

	//--------Datepicker funkcija----------------------------------------------------------------------
	$scope.openDatepicker = function($event, opened) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope[opened] = true;
	};
	//-------------------------------------------------------------------------------------------------

//----------------Dodavanje i brisanje deo--------------------------------------------------

$scope.openModal = function (deo, size) {

  //promenljiva sa svim definicijama
  // $scope.listaDefinicija = [{id: 'choice1'}];

  $scope.dodajDefiniciju = function($event) {
  	$event.preventDefault();
	$event.stopPropagation();
    //var newItemNo = $scope.akt.Uvodni_deo.Definicija.length+1;
    $scope.akt.Uvodni_deo.Definicija.push('');
  };

  $scope.ukloniDefiniciju = function($event) {
  	$event.preventDefault();
	$event.stopPropagation();
    var lastItem = $scope.listaDefinicija.length-1;
    $scope.listaDefinicija.splice(lastItem);
  };
};

$scope.openModal = function (definicija, size) {

	var modalInstance = $modal.open({
		templateUrl: 'views/deo.html',
		controller: 'deoCtrl',
		size: size,
		resolve: {
			deo: function(){
				return deo;
			}
		}
	});
	modalInstance.result.then(function (data){
		var deo = data.deo;
		//ako stavka fakture nema id i ako je akcija 'save' znaci da je nova i dodaje se u listu. ako ima, svakako se manja u listi
		if(!deo.id && data.action === 'save'){
			$scope.akt.delovi.push(deo);
		}
		//ako stavka treba da se obrise izbaci se iz niza
		if(data.action === 'delete'){
			var index = $scope.akt.delovi.indexOf(
				deo);
			$scope.akt.delovi.splice(index, 1);

		}
	}, function () {
		$log.info('Modal dismissed at: ' + new Date());
	});
};


//-------------------------------------------------------------------------------------------------
//Dodavanje glave
$scope.openModalGlava = function (glava, size) {

	var modalInstance = $modal.open({
		templateUrl: 'views/glava.html',
		controller: 'glavaCtrl',
		size: size,
		resolve: {
			glava: function(){
				return glava;
			}
		}
	});
	modalInstance.result.then(function (data){
		var glava = data.glava;
		//ako stavka fakture nema id i ako je akcija 'save' znaci da je nova i dodaje se u listu. ako ima, svakako se manja u listi
		if(!glava.id && data.action === 'save'){
			$scope.akt.glave.push(glava);
		}
		//ako stavka treba da se obrise izbaci se iz niza
		if(data.action === 'delete'){
			var index = $scope.akt.glave.indexOf(
				glava);
			$scope.akt.glave.splice(index, 1);

		}
	}, function () {
		$log.info('Modal dismissed at: ' + new Date());
	});
};

//Dodavanje odeljka

$scope.openModalOdeljak = function (odeljak, size){
	var modalInstance = $modal.open({
		templateUrl: 'views/odeljak.html',
		controller: 'odeljakCtrl',
		size: size,
		resolve: {
			odeljak: function(){
				return odeljak;
			}
		}
	});

	modalInstance.result.then(function(data){
		var odeljak = data.odeljak;
		if(!odeljak.id && data.action === 'save'){
			$scope.akt.odeljci.push(odeljak);
		}

		if(data.action === 'delete'){
			var index = $scope.akt.odeljci.indexOf(odeljak);
			$scope.akt.odeljci.splice(index, 1);
		}

	}, function(){
		$log.info('Modal dismissed at: ' + new Date());
	});
};




///////////////////




//----------------Dodavanje i brisanje glave--------------------------------------------------
$scope.listaglave = [{id: 'glava1'}];

$scope.dodajglava = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var newItemNo = $scope.listaglave.length+1;
	$scope.listaglave.push({'id':'choice'+newItemNo})
};

$scope.ukloniglava = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var lastItem = $scope.listaglave.length-1;
	$scope.listaglave.splice(lastItem);
};

//-------------------------------------------------------------------------------------------------


//----------------Dodavanje i brisanje kaznenih radnji i sankcija----------------------------------
$scope.listaradnjiISankcija = [{id: 'radnjaISankcija'}];

$scope.dodajRadnjaISankcija = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var newItemNo = $scope.listaradnjiISankcija.length+1;
	$scope.listaradnjiISankcija.push({'id':'choice'+newItemNo})
};


$scope.ukloniRadnjaISankcija = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var lastItem = $scope.listaradnjiISankcija.length-1;
	$scope.listaradnjiISankcija.splice(lastItem);
};

//-------------------------------------------------------------------------------------------------


//----------------Dodavanje normi-------------------------------------------------------------------
$scope.listaNormi = [{id: 'norma'}];

$scope.dodajNormu = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var newItemNo = $scope.listaNormi.length+1;
	$scope.listaNormi.push({'id':'choice'+newItemNo})
};

$scope.ukloniNormu = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var lastItem = $scope.listaNormi.length-1;
	$scope.listaNormi.splice(lastItem);
};

//-------------------------------------------------------------------------------------------------
//----------------Dodavanje kaznenih odredbi-------------------------------------------------------
$scope.listaKaznenihOdredba = [{id: 'odredba'}];

$scope.dodajKaznenuOdredbu = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var newItemNo = $scope.listaKaznenihOdredba.length+1;
	$scope.listaKaznenihOdredba.push({'id':'choice'+newItemNo})
};

$scope.ukloniKaznenuOdredbu = function($event){
	$event.preventDefault();
	$event.stopPropagation();
	var lastItem = $scope.listaKaznenihOdredba.length-1;
	$scope.listaKaznenihOdredba.splice(lastItem);
};



//-------------------------------------------------------------------------------------------------

	$scope.save = function () {



		if($scope.akt.id){
			$log.info($scope.akt);

			$scope.akt.$update({aktId:$scope.akt.id},function () {
				$location.path('/akt-list');
			});
		}
		else{
			$scope.akt.$save(function () {
				$location.path('/akt-list');
			});
		}
		$log.info("save");
	}

	$scope.delete = function () {
		if($scope.akt.id){
			$scope.akt.$delete({aktId:$scope.akt.id}, function () {
				$location.path('/akt-list');
			});
		}
	};




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





});
