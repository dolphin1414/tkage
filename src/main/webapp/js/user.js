var app = angular.module("serj.user", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("login", {
		url : "/login",
		views : {
			"main" : {
				templateUrl : "templates/loginForm.html",
				controller : "loginController",
				controllerAs : "bioCtrl"
			}
		},
		data : {
			pageTitle : "Вход"
		}
	})
	.state("register", {
		url : "/register",
		views : {
			"main" : {
				templateUrl : "templates/registrationForm.html",
				controller : "registerController",
				controllerAs : "bioCtrl"
			}
		},
		data : {
			pageTitle : "Регистрация"
		}
	});
});

app.controller("loginController", function(userService, $state) {
	this.login = function() {
		userService.login(this.user);
		$state.go("home");
	};
});

app.controller("registerController", function(userService, $state) {
	this.register = function() {
		userService.register(this.user);
		$state.go("home");
	};
});

app.factory("userService", function() {
	var service = {};
	var principal = null;
	
	service.login = function(user) {
		
		alert("Добро пожаловать на сайт наиталантливешего и наисветлейшего певца современности, уважаемый " + user.username);
		principal = user;
	};
	
	service.logout = function() {
		principal = null;
	}
	
	service.register = function(user) {
		//some actions
		service.login(user);
	};
	
	service.isLoggedIn = function() {
		return (principal != null);
	};
	
	return service;
});