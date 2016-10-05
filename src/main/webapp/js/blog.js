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

app.controller("blogController", function(blogService, $state) {
	var ctrl = this;
	ctrl.pageSize = 5;
	ctrl.pageNumber = 1;
	
	showPaginatedBlogs = function() {
		ctrl.data = blogService.getAllBlogs(ctrl.pageNumber, ctrl.pageSize);
	}
	
	this.showNextPage = function() {
		blogFromServer = blogService.getAllBlogs(ctrl.pageNumber + 1, ctrl.pageSize)
		blogFromServer.$promise.then(function() {
			ctrl.pageNumber += 1;
			showPaginatedBlogs();
		});
	}
	
	this.showPrevPage = function() {
		if (ctrl.pageNumber > 1) {
			ctrl.pageNumber -= 1;
			showPaginatedBlogs();
		} else {
			ctrl.pageNumber = 1;
		}
	}
	
	showPaginatedBlogs();
	
	ctrl.addNewBlog = function() {
		blogService.addNewBlog(ctrl.newBlog, function() { //success
			ctrl.newBlog = null;
			$state.reload();
		}, function() { // failure
			alert("Can not add blog");
		});
	}
	
	ctrl.updateBlog = function(blog) {
		blogService.updateBlog(blog, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not update blog");
		});
	}
	
	ctrl.deleteBlog = function(blog) {
		blogService.deleteBlog(blog, function() { //success
			$state.reload();
		}, function() { // failure
			alert("Can not delete <p>Йо-хо-хо!!!</p>");
		});
	}
});

app.service("blogService", function($resource) {
	var service = {};
	var Blogs = $resource("api/v1/resources/blogs");
	var Blog = $resource("api/v1/resources/blogs/:blogEntryId", null, {
	    'update': {method: 'PUT'}
	});
	var BlogComments = $resource("api/v1/resources/blogs/:blogEntryId/comments");
	
	service.getAllBlogs = function(pageNumber, pageSize) {
		return Blogs.query({
			"pageNumber" : pageNumber,
			"pageSize" : pageSize
		});
	}
	
	service.getBlogById = function(blogEntryId) {
		return Blog.get({blogEntryId:blogEntryId});
	}
	
	service.addNewBlog = function(blog, success, failure) {
		return Blogs.save(blog, success, failure);
	}
	
	service.updateBlog = function(blog, success, failure) {
		return Blog.update({blogEntryId:blog.blogId}, blog, success, failure);
	}
	
	service.deleteBlog = function(blog, success, failure) {
		return Blog.remove({blogEntryId:blog.blogId}, blog, success, failure);
	}
	
	service.getPaginatedCommentsForBlogId = function(blogId, pageNumber, pageSize) {
		return BlogComments.query({
			"blogEntryId" : blogId,
			"pageNumber" : pageNumber,
			"pageSize" : pageSize
		});
	}
	
	service.addNewBlogComment = function(blogId, comment, success, failure) {
		return BlogComments.save({"blogEntryId" : blogId}, comment, success, failure);
	}
	
	return service;
});

