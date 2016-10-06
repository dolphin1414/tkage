var app = angular.module("serj.blogDetails", [ "ngResource", "ui.router",
		"serj.blog" ]);

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

app.controller("blogDetailsController", function(commentService, blogService,
		$stateParams, $state, $rootScope, $interval, $scope) {
	var ctrl = this;
	ctrl.blog = blogService.getBlogById($stateParams.blogId);
	ctrl.comments = blogService.getPaginatedCommentsForBlogId(
			$stateParams.blogId, 1, 9999);
	ctrl.sendNewComment = function() {
		// ctrl.newComment.blogEntry.blogId = $stateParams.blogId;
		blogService.addNewBlogComment($stateParams.blogId, ctrl.newComment,
				function() { // success
					$state.reload();
				}, function() { // failure
					alert("Can not add comment((");
				})
		ctrl.newComment = null;
	}

	ctrl.deleteComment = function(comment) {
		commentService.deleteComment(comment, function() {
			$state.reload();
		}, function() {
			alert("Can not delete comment((");
		})
	}
	
	//TIMER UPDATES
	var displayedComments;
	var timerActions = 0;
	$scope.Timer = $interval(function() {
		timerActions = timerActions + 1;
		if (timerActions % refreshSteps(timerActions) === 0) {
			displayedComments = blogService.getPaginatedCommentsForBlogId(
					$stateParams.blogId, 1, 9999);
			displayedComments.$promise.then(function(result) {
				if (ctrl.comments.length === 0) {
					ctrl.comments = result;
					timerActions = 0;
				} else if (ctrl.comments[0].content != result[0].content) {
					ctrl.comments = result;
					timerActions = 0;
				}
			});
		}
	}, 1000);
	
	$scope.$on("$destroy",function(){
	    if (angular.isDefined($scope.Timer)) {
	        $interval.cancel($scope.Timer);
	    }
	});
});

app.service("commentService", function($resource) {
	service = {};
	Comments = $resource("api/v1/resources/comments/:commentId", null, {
		'update' : {
			method : 'PUT'
		}
	});

	service.deleteComment = function(comment, success, failure) {
		Comments.remove({
			commentId : comment.commentId
		}, comment, success, failure)
	}

	return service;
});

function arraysEqual(a, b) {
	if (a === b)
		return true;
	if (a == null || b == null)
		return false;
	if (a.length != b.length)
		return false;

	// If you don't care about the order of the elements inside
	// the array, you should sort both arrays here.

	for (var i = 0; i < a.length; ++i) {
		if (a[i] !== b[i])
			return false;
	}
	return true;
}

function refreshSteps(a) {
	if (a < 20) {
		return 5;
	}
	if (a < 100) {
		return 10;
	}
	if (a < 1000) {
		return 60;
	}
	return 1000;
}