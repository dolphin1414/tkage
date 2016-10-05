var app = angular.module("serj.photo", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("photo", {
		url : "/photo",
		views : {
			"main" : {
				templateUrl : "templates/photo.html",
				controller : "photoController",
				controllerAs : "photoCtrl"
			}
		},
		data : {
			pageTitle : "Фото"
		}
	});
});

app.controller("photoController", function(photoService, $state) {
	var ctrl = this;
	ctrl.pageSize = 5;
	ctrl.pageNumber = 1;
	
	showPaginatedPhoto = function() {
		ctrl.data = photoService.getAllPhoto(ctrl.pageNumber, ctrl.pageSize);
	}
	
	this.showNextPage = function() {
		photoFromServer = photoService.getAllPhoto(ctrl.pageNumber + 1, ctrl.pageSize)
		photoFromServer.$promise.then(function() {
			ctrl.pageNumber += 1;
			showPaginatedPhoto();
		});
	}
	
	this.showPrevPage = function() {
		if (ctrl.pageNumber > 1) {
			ctrl.pageNumber -= 1;
			showPaginatedPhoto();
		} else {
			ctrl.pageNumber = 1;
		}
	}
	
	showPaginatedPhoto();
	
	ctrl.addNewPhoto = function() {
		photoService.addNewPhoto(ctrl.newPhoto, function() { //success
			ctrl.newPhoto = null;
			$state.reload();
		}, function() { // failure
			alert("Can not add photo");
		});
	}
	
	ctrl.updatePhoto = function(photo) {
		photoService.updatePhoto(photo, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not update photo");
		});
	}
	
	ctrl.deletePhoto = function(photo) {
		photoService.deletePhoto(photo, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not delete photo");
		});
	}
});

app.service("photoService", function($resource) {
	var service = {};
	var Photos = $resource("api/v1/resources/photo");
	var Photo = $resource("api/v1/resources/photo/:photoEntryId", null, {
	    'update': {method: 'PUT'}
	});
	
	service.getAllPhoto = function(pageNumber, pageSize) {
		return Photos.query({
			"pageNumber" : pageNumber,
			"pageSize" : pageSize
		});
	}
	
	service.getPhotoById = function(photoEntryId) {
		return Photo.get({photoEntryId:photoEntryId});
	}
	
	service.addNewPhoto = function(photo, success, failure) {
		return Photos.save(photo, success, failure);
	}
	
	service.updatePhoto = function(photo, success, failure) {
		return Photo.update({photoEntryId:photo.photoEntryId}, photo, success, failure);
	}
	
	service.deletePhoto = function(photo, success, failure) {
		return Photo.remove({photoEntryId:photo.photoEntryId}, photo, success, failure);
	}
	
	return service;
});