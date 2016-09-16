var app = angular.module("serj.blogDetails", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("blogDetails", {
		url : "/blog/:blogId",
		views : {
			"main" : {
				templateUrl : "templates/blogDetails.html",
				controller : "blogDetailsController",
				controllerAs : "blogDetailsCtrl"
			}
		},
		data : {
			pageTitle : "Блог"
		}
	});
});

app.controller("blogDetailsController", function() {
	this.data = "Hello!!!";
});