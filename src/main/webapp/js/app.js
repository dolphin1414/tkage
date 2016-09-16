var app = angular.module("serj", ["ui.router", "serj.bio", "serj.contacts",
                                  "serj.video", "serj.blog", "serj.photo",
                                  "serj.blogDetails", "serj.user"]);

app.config(function($stateProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $locationProvider) {
	
	//Global properties
	$urlRouterProvider.otherwise("/");
	$urlMatcherFactoryProvider.caseInsensitive(true);
	$locationProvider.html5Mode(true);
	
	$stateProvider.state("home", {
		url: "/",
		views: {
			"main": {
				controller: "homeController",
				controllerAs: "homeCtrl"
			}
		}, 
		data : {
			pageTitle : "Home"
		}
	});
	
})

.run(function($rootScope, $state, userService) {
	//Чуть-чуть быдлокода, чтоб убрать адресную строку в мобильном браузере
	window.addEventListener("load",function() {
		setTimeout(function(){
			window.scrollTo(0, 1);
		}, 0);
	});
	$rootScope.logout = userService.logout;
	$rootScope.isLoggedIn = userService.isLoggedIn;
});

app.controller("homeController", function() {
	this.data = "Hello!!!";
});

app.controller("appCtrl", function AppCtrl($interval, $scope, $location, $state, $stateParams, $rootScope, userService) {
	$scope.$on("$stateChangeSuccess", function(event, toState, toParams, fromState, fromParams) {
		$interval.cancel($rootScope.timer);
		if (angular.isDefined(toState.data.pageTitle)) {
				$scope.pageTitle = toState.data.pageTitle;
		}
	});
	
});
