var app = angular.module("serj.user", [ "ngResource", "ui.router" ]);

app.config(function($stateProvider) {
	
	$stateProvider.state("login", {
		url : "/login",
		views : {
			"main" : {
				templateUrl : "templates/loginForm.html",
				controller : "loginController",
				controllerAs : "loginCtrl"
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
				controllerAs : "registerCtrl"
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

app.factory("userService", function($http, $resource) {
	var service = {};
	var principal = null;
	var Users = $resource("/api/v1/resources/users");
	var User = $resource("/api/v1/resources/users/:name");
	//TODO save username in local storage after login and reload it after refreshing
	
	service.login = function(user) {
		return $http.post("/login", "username=" + user.username + "&password=" + user.password,
				{headers: {"Content-Type": "application/x-www-form-urlencoded"}})
				.then(function(response) { // OK
					principal = response.data;
					alert("Добро пожаловать, " + user.username + "!");
				}, function(response) { // Problem
					alert("There are some problems with username. Reason: " + response.statusText);
					console.dir(response);
				});
	}
	
	service.logout = function() {
		$http.get("/logout");
		principal = null;
	}
	
	service.register = function(user) {
		service.userExists(user, function(data) { //user exists
			alert("Cannot register new user. Reason: user " + user.username 
					+ " is already registered");
		}, function(data) { //user not exists
			Users.save({}, user, function() { // POST new user success
				alert("Поздравляем, " + user.username + "! Вы успешно зарегистрировались! Теперь можете писать комментарии в блоге Сергея.");
				service.login(user);
			}, function(data) { // POST new user failure
				alert("There are some problems with register user. Reason: " + data.statusText);
			});
		});
	}
	
	service.isLoggedIn = function() {
		return (principal != null);
	}
	
	service.isEditor = function() {
		return (principal != null) && (principal.roles.indexOf("ROLE_EDITOR") != -1);
	}
	
	service.isAdmin = function() {
		return (principal != null) && (principal.roles.indexOf("ROLE_ADMIN") != -1);
	}
	
	service.isAuthor = function(name) {
		return (principal != null) && (principal.username === name);
	}
	
	service.userExists = function(user, success, failure) {
		User.get({name:user.username}, 
					function(data) {
						if (data.username === user.username) {
							success(data);
						} else {
							failure();
						}
					}, 
					failure);
	}
	return service;
});

app.directive("passwordVerify", function() {
	   return {
	      require: "ngModel",
	      scope: {
	        passwordVerify: '='
	      },
	      link: function(scope, element, attrs, ctrl) {
	        scope.$watch(function() {
	            var combined;

	            if (scope.passwordVerify || ctrl.$viewValue) {
	               combined = scope.passwordVerify + '_' + ctrl.$viewValue; 
	            }                    
	            return combined;
	        }, function(value) {
	            if (value) {
	                ctrl.$parsers.unshift(function(viewValue) {
	                    var origin = scope.passwordVerify;
	                    if (origin !== viewValue) {
	                        ctrl.$setValidity("passwordVerify", false);
	                        return undefined;
	                    } else {
	                        ctrl.$setValidity("passwordVerify", true);
	                        return viewValue;
	                    }
	                });
	            }
	        });
	     }
	   };
	});