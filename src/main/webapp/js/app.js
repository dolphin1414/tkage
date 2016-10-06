var app = angular.module("serj", [ "ui.router", "serj.bio", "serj.contacts",
		"serj.video", "serj.blog", "serj.photo", "serj.blogDetails",
		"serj.user" ]);

app.config(
		function($stateProvider, $urlRouterProvider,
				$urlMatcherFactoryProvider, $locationProvider) {

			// Global properties
			$urlRouterProvider.otherwise("/");
			$urlMatcherFactoryProvider.caseInsensitive(true);
			$locationProvider.html5Mode(true);

			$stateProvider.state("home", {
				url : "/",
				views : {
					"main" : {
						controller : "homeController",
						controllerAs : "homeCtrl"
					}
				},
				data : {
					pageTitle : "Home"
				}
			});

		})

.run(function($rootScope, $state, userService) {
	// Remove browser address bar
	window.addEventListener("load", function() {
		setTimeout(function() {
			window.scrollTo(0, 1);
		}, 0);
	});
	$rootScope.logout = userService.logout;
	$rootScope.isLoggedIn = userService.isLoggedIn;
	$rootScope.isEditor = userService.isEditor;
	$rootScope.isAdmin = userService.isAdmin;
	$rootScope.isAuthor = userService.isAuthor;
});

app.controller("homeController", function() {
	this.data = "Hello!!!";
});

app.controller("appCtrl", function AppCtrl($interval, $scope, $location,
		$state, $stateParams, $rootScope, userService) {
	$scope.$on("$stateChangeSuccess", function(event, toState, toParams,
			fromState, fromParams) {
		if (angular.isDefined(toState.data.pageTitle)) {
			$scope.pageTitle = toState.data.pageTitle;
		}
	});

});

app.filter("trust", [ '$sce', function($sce) {
	return function(htmlCode) {
		return $sce.trustAsHtml(htmlCode);
	}
} ]);
