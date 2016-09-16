var app = angular.module("serj.blog", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("blog", {
		url : "/blog",
		views : {
			"main" : {
				templateUrl : "templates/blog.html",
				controller : "blogController",
				controllerAs : "blogCtrl"
			}
		},
		data : {
			pageTitle : "Блог"
		}
	});
});

app.controller("blogController", function() {
	this.blog = {};
	this.blog.blogId = 1;
	console.log(this.blog.blogId);
});

