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

package tk.serjmusic.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tk.serjmusic.dao.BlogCommentDao;
import tk.serjmusic.dao.BlogEntryDao;
import tk.serjmusic.dao.UserDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.models.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * This is the main testing class for DAO layer due to it tests generic logic implemented in
 * {@link AbstractGenericDao}.
 * </p>
 * 
 * <p>
 * For all another classes in {@link tk.serjmusic.dao.impl} only custom methods should be tested.
 * </p>
 *
 * @author Roman Kondakov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
@Transactional
@Rollback
public class UserDaoImplTest {

    // init test data
    private static final String PERSISTED_USERNAME = "John_Doe";
    private static final String TRANSIENT_USERNAME_1 = "Jane_Doe";
    private static final String TRANSIENT_USERNAME_2 = "Jim_Doe";
    private static final String PERSISTED_EMAIL = "john.doe@mail.com";
    private User persisted = null;
    private User transient1 = new User(TRANSIENT_USERNAME_1);
    private User transient2 = new User(TRANSIENT_USERNAME_2);
    private BlogEntry blog = new BlogEntry("test blog");
    private BlogComment blogComment1 = new BlogComment("test comment 1");
    private BlogComment blogComment2 = new BlogComment("test comment 2");
    private BlogComment blogComment3 = new BlogComment("test comment 3");
    Set<BlogComment> comments =
            new HashSet<>(Arrays.asList(blogComment1, blogComment2, blogComment3));
    @Autowired
    UserDao userDao;

    @Autowired
    BlogCommentDao commentDao;

    @Autowired
    BlogEntryDao blogDao;


    /**
     * Set up method - init database with user entity.
     * 
     */
    @Commit
    @Before
    public void setUp() {
        
        // set peristed user data
        persisted = new User(PERSISTED_USERNAME);
        persisted.setEmail(PERSISTED_EMAIL);
        persisted.setPassword("test");
        persisted.setRoles(new HashSet<>(Arrays.asList(UserRole.ROLE_ADMIN)));
        persisted.setBlogs(new HashSet<>(Arrays.asList(blog)));
        persisted.setComments(comments);
        
        // set transient users data
        transient1.setEmail("test_email_1");
        transient2.setEmail("test_email_2");
        
        // set blog data
        blog.setContent("test_content_1");
        blog.setAuthor(persisted);
        blog.setComments(new ArrayList<>(comments));
        
        // set blog comments
        blogComment1.setContent("test_content_1");
        blogComment2.setContent("test_content_2");
        blogComment3.setContent("test_content_3");
        blogComment1.setBlogEntry(blog);
        blogComment2.setBlogEntry(blog);
        blogComment3.setBlogEntry(blog);
        blogComment1.setAuthor(persisted);
        blogComment2.setAuthor(persisted);
        blogComment3.setAuthor(persisted);
        
        // persist entities
        commentDao.persist(blogComment1);
        commentDao.persist(blogComment2);
        commentDao.persist(blogComment3);
        userDao.persist(persisted);
        blogDao.persist(blog);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.UserDaoImpl#findUserByUsername(java.lang.String)}.
     */
    @Test
    @Transactional
    public final void testfindUserByUsername() {
        assertEquals(persisted, userDao.findUserByUsername(PERSISTED_USERNAME));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.UserDaoImpl#getUserByEmail(java.lang.String)}.
     */
    @Test
    public final void testFindUserByEmail() {
        assertEquals(persisted, userDao.findUserByEmail(PERSISTED_EMAIL));
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.UserDaoImpl#getUserCommentsByUserId(int, int, int)}.
     */
    @Test
    public final void testFindUserCommentsByUserId() {
        List<BlogComment> comments = userDao.findUserCommentsByUserId(persisted.getId(), 1, 5);
        assertEquals(3, comments.size());
        assertEquals(blogComment1, comments.get(0));
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.AbstractGenericDao#persist(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testPersist() {
        userDao.persist(transient1);
        assertEquals(transient1, userDao.find(transient1.getId()));
        assertEquals(persisted, userDao.find(persisted.getId()));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.AbstractGenericDao#find(int)}.
     */
    @Test
    public final void testFind() {
        assertEquals(persisted, userDao.find(persisted.getId()));
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.AbstractGenericDao#merge(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testMerge() {
        final String newName = "John_Newman";
        persisted = userDao.findUserByUsername(PERSISTED_USERNAME);
        persisted.setUsername(newName);
        userDao.merge(persisted);
        assertNotNull(userDao.findUserByUsername(newName));
        assertNull(userDao.findUserByUsername(PERSISTED_USERNAME));
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.AbstractGenericDao#remove(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testRemove() {
        assertEquals(1, userDao.findAll().size());
        userDao.remove(userDao.findUserByUsername(PERSISTED_USERNAME));
        assertEquals(0, userDao.findAll().size());
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.AbstractGenericDao#findAll()}.
     */
    @Test
    public final void testFindAll() {
        userDao.persist(transient1);
        userDao.persist(transient2);
        List<User> users = userDao.findAll();
        assertEquals(3, users.size());
        assertTrue(users.contains(persisted));
        assertTrue(users.contains(transient1));
        assertTrue(users.contains(transient2));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.AbstractGenericDao#countAll()}.
     */
    @Test
    public final void testCountAll() {
        assertEquals(1, userDao.countAll());
        userDao.persist(transient1);
        assertEquals(2, userDao.countAll());
        userDao.persist(transient2);
        assertEquals(3, userDao.countAll());
    }

    /**
     * Test method for
     * {@link tk.serjmusic.dao.impl.AbstractGenericDao#findPaginatedAndOrdered(boolean, int, int)}.
     */
    @Test
    public final void testFindPaginatedAndOrdered() {
        userDao.persist(transient1);
        userDao.persist(transient2);
        List<User> users = userDao.findPaginatedAndOrdered(true, 1, 2);
        assertEquals(2, users.size());
        assertEquals(persisted, users.get(0));
        assertEquals(transient1, users.get(1));
        users = userDao.findPaginatedAndOrdered(true, 2, 2);
        assertEquals(1, users.size());
        assertEquals(transient2, users.get(0));
        users = userDao.findPaginatedAndOrdered(false, 1, 1);
        assertEquals(transient2, users.get(0));
    }
    
    /**
     * Test method for
     * {@link tk.serjmusic.dao.BlogEntryDaoImpl#findPaginatedCommentsForBlogId(int, int, int)}.
     */
    @Test
    public final void testFindPaginatedCommentsForBlogId() {
        List<BlogComment> derivedcommentsomments = 
                blogDao.findPaginatedCommentsForBlogId(blog.getId(), 1, 2);
        assertEquals(2, derivedcommentsomments.size());
        assertTrue(derivedcommentsomments.contains(blogComment1));
        assertTrue(derivedcommentsomments.contains(blogComment2));
        derivedcommentsomments = blogDao.findPaginatedCommentsForBlogId(blog.getId(), 2, 2);
        assertEquals(1, derivedcommentsomments.size());
        assertTrue(derivedcommentsomments.contains(blogComment3));
    }

}
