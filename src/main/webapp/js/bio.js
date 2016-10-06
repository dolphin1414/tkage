var app = angular.module("serj.bio", [ "ngResource", "ui.router", "ngSanitize"]);

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

app.controller("bioController", function(staticContentService, $sce) {
	var ctrl = this;
	
	this.data = staticContentService.getStaticContentByName("bio", function(data) {
		ctrl.parsibleData = data.content;
		ctrl.staticContentId = data.staticContentId;
	}, function() {});
	
	this.update = function() {
		staticContentService.updateStaticContent(ctrl.staticContentId, 
				{"content": ctrl.parsibleData},
				function() {alert("Сохранено!");}, function() {alert("Какие-то проблемы - не сохранено!");});
	}
});

app.factory("staticContentService", function($resource) {
	var service = {};
	var StaticContent = $resource("api/v1/resources/static/:contentName", null, {
	    'update': { method:'PUT' }
	});
	
	service.getStaticContentByName = function(contentName, success, failure) {
		return StaticContent.get({contentName:contentName}, success, failure);
	}
	
	service.updateStaticContent = function(contentName, postData, success, failure) {
		return StaticContent.update({contentName:contentName}, postData, success, failure);
	};
	
	return service;
});