var app = angular.module("serj.contacts", [ "ngResource", "ui.router", "serj.bio" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("contacts", {
		url : "/contacts",
		views : {
			"main" : {
				templateUrl : "templates/contacts.html",
				controller : "contactsController",
				controllerAs : "contactsCtrl"
			}
		},
		data : {
			pageTitle : "Contacts"
		}
	});
});

app.controller("contactsController", function(staticContentService) {
	var ctrl = this;
	
	ctrl.data = staticContentService.getStaticContentByName("contacts", function(data) {
		ctrl.contactData = data.content;
		ctrl.staticContentId = data.staticContentId;
	}, function() {
		alert("can not find contact data((");
	});
	
	ctrl.update = function() {
		staticContentService.updateStaticContent(ctrl.staticContentId, 
				{"content": ctrl.contactData},
				function() {alert("success!");}, function() {alert("failure!");});
	}
});