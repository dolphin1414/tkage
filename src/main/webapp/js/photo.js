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

app.controller("photoController", function() {
	this.data = "Hello!!!";
});