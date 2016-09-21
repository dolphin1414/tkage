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

package tk.serjmusic.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tk.serjmusic.dao.BlogEntryDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

/**
 * Test case for {@link BlogEntryServiceImpl). Other methods are tested in 
 * common logic test case {@link UserServiceImplTest}.
 *
 * @author Roman Kondakov
 */
public class BlogEntryServiceImplTest {
    
    @Mock
    private BlogEntryDao blogDao;

    @InjectMocks
    private BlogEntryServiceImpl blogService;

    // test data declaration
    private BlogEntry blogEntry;
    private BlogComment blogComment1;
    private BlogComment blogComment2;
    private List<BlogComment> comments;

    /**
     * Set up method.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // init test data
        blogComment1 = new BlogComment("test_comment_1");
        blogComment2 = new BlogComment("test_comment_2");
        blogEntry = new BlogEntry("test_blog_entry");
        comments = new ArrayList<>(Arrays.asList(blogComment1, blogComment2));
        blogEntry.setComments(comments);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.BlogEntryServiceImpl
     * #getPaginatedCommentsForBlogId(int, int, int)}
     * .
     */
    @Test
    public final void testGetPaginatedCommentsForBlogId() {
        int blogId = 1;
        int pageNumber = 1;
        int pageSize = 2;
        // should be OK
        when(blogDao.findPaginatedCommentsForBlogId(blogId, pageNumber, pageSize))
                .thenReturn(comments);
        assertEquals(comments, 
                blogService.getPaginatedCommentsForBlogId(blogId, pageNumber, pageSize));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            blogService.getPaginatedCommentsForBlogId(-1, 0, pageSize);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        try {
            blogService.getPaginatedCommentsForBlogId(blogId, 0, pageSize);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException &= true;
        }
        try {
            blogService.getPaginatedCommentsForBlogId(blogId, pageNumber, -1);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException &= true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(blogDao.findPaginatedCommentsForBlogId(blogId, pageNumber, pageSize)).thenReturn(null);
        try {
            blogService.getPaginatedCommentsForBlogId(blogId, pageNumber, pageSize);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(blogDao.findPaginatedCommentsForBlogId(blogId, pageNumber, pageSize))
                .thenThrow(new PersistenceException());
        try {
            blogService.getPaginatedCommentsForBlogId(blogId, pageNumber, pageSize);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

}
