/**
* This is free and unencumbered software released into the public domain.
*
* Anyone is free to copy, modify, publish, use, compile, sell, or
* distribute this software, either in source code form or as a compiled
* binary, for any purpose, commercial or non-commercial, and by any
* means.
*
* In jurisdictions that recognize copyright laws, the author or authors
* of this software dedicate any and all copyright interest in the
* software to the public domain. We make this dedication for the benefit
* of the public at large and to the detriment of our heirs and
* successors. We intend this dedication to be an overt act of
* relinquishment in perpetuity of all present and future rights to this
* software under copyright law.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
* OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
* ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*
* For more information, please refer to <http://unlicense.org/>
*/

package tk.serjmusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tk.serjmusic.controllers.dto.BlogCommentDto;
import tk.serjmusic.controllers.dto.BlogEntryDto;
import tk.serjmusic.controllers.dto.asm.BlogCommentDtoAsm;
import tk.serjmusic.controllers.dto.asm.BlogEntryDtoAsm;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.services.BlogCommentService;
import tk.serjmusic.services.BlogEntryService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link BlogEntry} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/blogs")
public class BlogEntryController {
    
    private static final BlogEntryDtoAsm blogDtoAsm = new BlogEntryDtoAsm();

    @Autowired
    private BlogEntryService blogService;
    @Autowired
    private BlogCommentService commentService;

    /**
     * Get {@link ResponseEntity} with the paginated list of {@link BlogEntryDto} entities. 
     * For retrieving all blog entities please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link BlogEntryDto}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BlogEntryDto>> getPaginatedBlogs(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<BlogEntry> blogs = blogService.getPaginatedAndOrdered(R.DEFAULT_ASC_ID_SORT_ORDER,
                pageNumber, pageSize);
        List<BlogEntryDto> blogDtoList = blogDtoAsm.toResources(blogs);
        return new ResponseEntity<List<BlogEntryDto>>(blogDtoList, HttpStatus.OK);
    }

    /**
     * Create new {@link BlogEntry}.
     * 
     * @param blogDto - the {@link BlogEntryDto} for the {@link BlogEntry} to be created
     * @return {@link ResponseEntity} with created {@link BlogEntryDto}
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BlogEntryDto> addNewBlogEntry(@RequestBody BlogEntryDto blogDto) {
        if (blogDto == null) {
            throw new IllegalArgumentException("Blog DTO should not be null");
        }
        BlogEntry blog = blogDto.overwriteEntity(new BlogEntry());
        blog = blogService.create(blog);
        return new ResponseEntity<BlogEntryDto>(blogDtoAsm.toResource(blog), HttpStatus.OK);
    }
    
    /**
     * Get {@link BlogEntry} for its ID.
     * 
     * @param blogId - the ID of needed blog entry
     * @return - {@link ResponseEntity} with found {@link User}
     */
    @RequestMapping(path = "/{blogId}", method = RequestMethod.GET)
    public ResponseEntity<BlogEntryDto> getBlogById(@PathVariable("blogId") int blogId) {
        if (blogId < 0) {
            throw new IllegalArgumentException(
                    "Blog ID should be greater than 0, but have:" + blogId);
        }
        BlogEntry blog = blogService.getById(blogId);
        return new ResponseEntity<BlogEntryDto>(blogDtoAsm.toResource(blog), HttpStatus.OK);
    }

    /**
     * Update {@link BlogEntry}.
     * 
     * @param blogDto - the {@link BlogEntryDto} of the {@link BlogEntry} to be updated
     * @return {@link BlogEntryDto} of updated {@link BlogEntry}
     */
    @RequestMapping(path = "/{blogId}", method = RequestMethod.PUT)
    public ResponseEntity<BlogEntryDto> updateBlogById(@RequestBody BlogEntryDto blogDto) {
        if (blogDto == null) {
            throw new IllegalArgumentException("BlogDto should not be null");
        }
        BlogEntry blog = blogService
                .update(blogDto.overwriteEntity(blogService.getById(blogDto.getBlogId())));
        return new ResponseEntity<BlogEntryDto>(blogDtoAsm.toResource(blog), HttpStatus.OK);
    }

    /**
     * Delete {@link BlogEntry} from persistent context.
     * 
     * @param blogId - the ID of the {@link BlogEntry} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{blogId}", method = RequestMethod.DELETE)
    public ResponseEntity<BlogEntryDto> deleteUserById(@PathVariable("blogId") int blogId) {
        if (blogId < 0) {
            throw new IllegalArgumentException(
                    "User id should be greater than 0," + " but have:" + blogId);
        }
        blogService.delete(blogService.getById(blogId));
        return new ResponseEntity<BlogEntryDto>(HttpStatus.OK);
    }
    
    /**
     * Get {@link ResponseEntity} with the paginated list of {@link BlogCommentDto} entities.
     * For retrieving all blog comments please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link BlogCommentDto}
     */
    @RequestMapping(path = "/{blogId}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<BlogCommentDto>> getPaginatedCommentsForBlog(
            @PathVariable("blogId") int blogId,
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1) || (blogId < 0)) {
            throw new IllegalArgumentException("pageNumber, pageSize should be > 0, blogId > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize 
                    + ", blogId=" + blogId);
        }
        List<BlogComment> comments = blogService
                .getPaginatedCommentsForBlogId(blogId, pageNumber, pageSize);
        List<BlogCommentDto> commentDtoList = new BlogCommentDtoAsm().toResources(comments);
        return new ResponseEntity<List<BlogCommentDto>>(commentDtoList, HttpStatus.OK);
    }
    
    /**
     * Add new comment to the blog entry.
     * 
     * @param blogId - the id of blog entry
     * @param commentDto - comment DTO
     * @return {@link ResponseEntity} with {@link BlogCommentDto}
     */
    @RequestMapping(path = "/{blogId}/comments", method = RequestMethod.POST)
    public ResponseEntity<BlogCommentDto> addNewCommentForBlogId(
            @PathVariable("blogId") int blogId, @RequestBody BlogCommentDto commentDto) {
        if (blogId < 0) {
            throw new IllegalArgumentException("Blog Entry ID should be >= 0"
                    + " but have  blogId=" + blogId);
        }
        if (commentDto == null) {
            throw new IllegalArgumentException("Comment DTO should not be null");
        }
        BlogComment comment = commentDto.overwriteEntity(new BlogComment());
        comment = commentService.create(comment);
        return new ResponseEntity<BlogCommentDto>(new BlogCommentDtoAsm().toResource(comment),
                HttpStatus.OK);
    }
}

