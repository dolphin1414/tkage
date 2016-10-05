var app = angular.module("serj.video", [ "ngResource", "ui.router", "ngSanitize" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("video", {
		url : "/video",
		views : {
			"main" : {
				templateUrl : "templates/video.html",
				controller : "videoController",
				controllerAs : "videoCtrl"
			}
		},
		data : {
			pageTitle : "Video"
		}
	});
});

app.controller("videoController", function(videoService, $sce, $state) {
	var ctrl = this;
	ctrl.pageSize = 5;
	ctrl.pageNumber = 1;
	
	showPaginatedVideo = function() {
		ctrl.data = videoService.getAllVideo(ctrl.pageNumber, ctrl.pageSize);
	}
	
	this.showNextPage = function() {
		videoFromServer = videoService.getAllVideo(ctrl.pageNumber + 1, ctrl.pageSize)
		videoFromServer.$promise.then(function() {
			ctrl.pageNumber += 1;
			showPaginatedVideo();
		});
	}
	
	this.showPrevPage = function() {
		if (ctrl.pageNumber > 1) {
			ctrl.pageNumber -= 1;
			showPaginatedVideo();
		} else {
			ctrl.pageNumber = 1;
		}
	}
	
	showPaginatedVideo();
	
	ctrl.addNewVideo = function() {
		videoService.addNewVideo(ctrl.newVideo, function() { //success
			ctrl.newVideo = null;
			$state.reload();
		}, function() { // failure
		});
	}
	
	ctrl.updateVideo = function(video) {
		videoService.updateVideo(video, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not update video");
		});
	}
	
	ctrl.deleteVideo = function(video) {
		videoService.deleteVideo(video, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not delete video");
		});
	}
});

app.service("videoService", function($resource) {
	var service = {};
	var Videos = $resource("api/v1/resources/video");
	var Video = $resource("api/v1/resources/video/:videoEntryId", null, {
	    'update': {method: 'PUT'}
	});
	
	service.getAllVideo = function(pageNumber, pageSize) {
		return Videos.query({
			"pageNumber" : pageNumber,
			"pageSize" : pageSize
		});
	}
	
	service.getVideoById = function(videoEntryId) {
		return Video.get({videoEntryId:videoEntryId});
	}
	
	service.addNewVideo = function(video, success, failure) {
		return Videos.save(video, success, failure);
	}
	
	service.updateVideo = function(video, success, failure) {
		return Video.update({videoEntryId:video.videoEntryId}, video, success, failure);
	}
	
	service.deleteVideo = function(video, success, failure) {
		return Video.remove({videoEntryId:video.videoEntryId}, video, success, failure);
	}
	
	return service;
});