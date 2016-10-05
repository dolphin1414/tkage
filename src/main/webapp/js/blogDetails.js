var app = angular.module("serj.blogDetails", [ "ngResource", "ui.router", "serj.blog" ]);

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

app.controller("blogDetailsController", function(commentService, blogService, $stateParams, $state) {
	var ctrl = this;
	ctrl.blog = blogService.getBlogById($stateParams.blogId);
	ctrl.comments = blogService.getPaginatedCommentsForBlogId($stateParams.blogId, 1, 9999);
	ctrl.sendNewComment = function() {
		//ctrl.newComment.blogEntry.blogId = $stateParams.blogId;
		blogService.addNewBlogComment($stateParams.blogId, ctrl.newComment, function() { //success
			$state.reload();
		}, function() { //failure
			alert("Can not add comment((");
		})
		ctrl.newComment = null;
	}
	
	ctrl.deleteComment = function(comment) {
		console.dir(comment); //TODO remove
		commentService.deleteComment(comment, function() {
			$state.reload();
		}, function() {
			alert("Can not delete comment((");
		})
	} 
});

app.service("commentService", function($resource) {
	service = {};
	Comments = $resource("api/v1/resources/comments/:commentId", null, {
	    'update': {method: 'PUT'}
	});
	
	service.deleteComment = function(comment, success, failure) {
		Comments.remove({commentId:comment.commentId}, comment, success, failure)
	}
	
	return service;
});