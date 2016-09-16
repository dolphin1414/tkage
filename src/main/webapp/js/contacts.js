var app = angular.module("serj.contacts", [ "ngResource", "ui.router" ]);

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

app.controller("contactsController", function() {
	this.data = "Hello!!!";
});