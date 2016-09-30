/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software, either
 * in source code form or as a compiled binary, for any purpose, commercial or non-commercial, and
 * by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this software dedicate
 * any and all copyright interest in the software to the public domain. We make this dedication for
 * the benefit of the public at large and to the detriment of our heirs and successors. We intend
 * this dedication to be an overt act of relinquishment in perpetuity of all present and future
 * rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
import tk.serjmusic.controllers.dto.UserDto;
import tk.serjmusic.controllers.dto.asm.BlogCommentDtoAsm;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.services.BlogCommentService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link BlogComment} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/comments")
public class BlogCommentController {

    private static final BlogCommentDtoAsm blogCommentDtoAsm = new BlogCommentDtoAsm();

    @Autowired
    private BlogCommentService commentsService;
    
    /**
     * Get {@link ResponseEntity} with the paginated list of {@link BlogCommentDto} entities. 
     * For retrieving all user entities please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}. If these parameters are missed, the default values are:
     * {@code pageNumber = 1} and {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link BlogCommentDto}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BlogCommentDto>> getPaginatedComments(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<BlogComment> comments = commentsService
                .getPaginatedAndOrdered(R.DEFAULT_ASC_ID_SORT_ORDER, pageNumber, pageSize);
        List<BlogCommentDto> commentDtoList = blogCommentDtoAsm.toResources(comments);
        return new ResponseEntity<List<BlogCommentDto>>(commentDtoList, HttpStatus.OK);
    }
    
    /**
     * Get {@link BlogCommentDto} for its ID.
     * 
     * @param commentId - the ID of needed blog comment
     * @return - {@link ResponseEntity} with found {@link BlogCommentDto}
     */
    @RequestMapping(path = "/{commentId}", method = RequestMethod.GET)
    public ResponseEntity<BlogCommentDto> getCommentById(@PathVariable("commentId") int commentId) {
        if (commentId < 0) {
            throw new IllegalArgumentException("Comment id should be greater than 0," 
                    + " but have:" + commentId);
        }
        BlogComment comment = commentsService.getById(commentId);
        BlogCommentDto commentDto = blogCommentDtoAsm.toResource(comment);
        return new ResponseEntity<BlogCommentDto>(commentDto, HttpStatus.OK);
    }
    
    /**
     * Update {@link BlogComment}.
     * 
     * @param blogCommentDto - the {@link BlogCommentDto} of the {@link BlogComment} to be updated
     * @return {@link BlogCommentDto} of updated {@link BlogComment}
     */
    @RequestMapping(path = "/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<BlogCommentDto> updateCommentById(
            @RequestBody BlogCommentDto blogCommentDto) {
        if (blogCommentDto == null) {
            throw new IllegalArgumentException("Blog comment should not be null");
        }
        BlogComment comment = commentsService.update(blogCommentDto
                .overwriteEntity(commentsService.getById(blogCommentDto.getCommentId())));
        return new ResponseEntity<BlogCommentDto>(blogCommentDtoAsm.toResource(comment), 
                HttpStatus.OK);
    }
    
    /**
     * Delete {@link BlogComment} from persistent context.
     * 
     * @param commentId - the ID of the {@link BlogComment} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> deleteCommentById(@PathVariable("commentId") int commentId) {
        if (commentId < 0) {
            throw new IllegalArgumentException(
                    "Comment id should be greater than 0," + " but have:" + commentId);
        }
        commentsService.delete(commentsService.getById(commentId));
        return new ResponseEntity<UserDto>(HttpStatus.OK);
    }

}
