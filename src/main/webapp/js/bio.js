var app = angular.module("serj.bio", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("bio", {
		url : "/bio",
		views : {
			"main" : {
				templateUrl : "templates/bio.html",
				controller : "bioController",
				controllerAs : "bioCtrl"
			}
		},
		data : {
			pageTitle : "Biography"
		}
	});
});

app.controller("bioController", function() {
	this.data = "Hello!!!";
});