var app = angular.module("serj.video", [ "ngResource", "ui.router" ]);

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

app.controller("videoController", function() {
	this.data = "Hello!!!";
});