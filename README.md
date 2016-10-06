# Summary
This is a very simple example of RESTful API based web application which was developed as a CMS for stand-alone blog. 
It allows blogger to share his photos, videos and thoughts with other users. And users can comment his entries and chat with each other.
Blogger and users should be registered and authenticated to post content with this application.

- Server side is built on the Spring MVC framework base: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html
- The base of the REST client front-end application is the AngularJS framework: https://angularjs.org/

## Server side

The application can be separated into several layers such as:

- Repository (persistence) layer
- Service layer
- Web layer

These layers are described below.

### Repository (persistence) layer

The responsibility of this layer is the handling entities in the persistence context. All entities used in this project are represented on this picture:

![alt tag](http://i.imgur.com/3PvXbEv.png)

For implementing persistence context API (JPA) these technologies was involved:

- ORM: Hibernate
- Cache: EhCache
- Connection pool: Apache Commons DBCP2

### Service layer

This layer implements a business logic of the application. 

### Web layer

The gate to the outer world for the application. This layer is implemented with Spring MVC and Security framework. 
It's responsible for implementing application endpoint REST API. The description of this API is listed below:

```
USERS API
api/v1/resources/users – GET - first 10 users, POST - create a new user
api/v1/resources/users/{userId} – GET, PUT, DELETE a concrete user 
api/v1/resources/users?pageNumber={int}&pageSize={int} – GET paginated users
api/v1/resources/users/{userId}/comments –  get first 10 user comments
api/v1/resources/users/{userId}/comments?pageNumber={int}&pageSize={int} –  GET paginated user comments

BLOG ENTRY API
api/v1/resources/blogs – GET - first 10 blog entries POST - create a new blog entry
api/v1/resources/blogs/{blogId} – GET, PUT, DELETE a concrete blog
api/v1/resources/blogs?pageNumber={int}&pageSize={int} – GET paginated blog
api/v1/resources/blogs/{blogId}/comments – GET first 10 comments for blog, POST create a new blog comment
api/v1/resources/blogs/{blogId}/comments/{commentId} – GET, PUT, DELETE a concrete blog comment
api/v1/resources/blogs/{blogId}/comments?pageNumber={int}&pageSize={int} – GET paginated comments

BLOG COMMENTS API
api/v1/resources/comments – GET - first 10 comments for all blogs 
api/v1/resources/comments/{commentId} – GET, PUT, DELETE a concrete comment
api/v1/resources/comments?pageNumber={int}&pageSize={int} – GET paginated comment

STATIC INFO API
api/v1/resources/static/ - GET all static info, POST a new static info entry
api/v1/resources/static/{contentDescription} - GET, PUT, DELETE concrete static info
examples:
api/v1/resources/static/bio – about me
api/v1/resources/static/contacts – contacts

VIDEO ENTRIES API
api/v1/resources/video – GET first 10 videos, POST - create a new video entry
api/v1/resources/video/{videoId} – GET, PUT, DELETE concrete video
api/v1/resources/video?pageNumber={int}&pageSize={int} – GET paginated video entries

PHOTO ENTRIES API
api/v1/resources/photo – GET first 10 photos, POST - create a new photo entry
api/v1/resources/photo/{photoId} – GET, PUT, DELETE concrete photo
api/v1/resources/photo?pageNumber={int}&pageSize={int} – GET paginated photo

```

## Client side

The client side is based on AngularJS framework with UI-Router (https://angular-ui.github.io/ui-router/site/) for site navigation. 
