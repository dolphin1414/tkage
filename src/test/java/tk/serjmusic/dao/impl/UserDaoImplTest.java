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

package tk.serjmusic.dao.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tk.serjmusic.dao.AbstractGenericDao;
import tk.serjmusic.dao.UserDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.models.UserRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * <p>This is the main testing class for DAO layer due to it tests generic logic 
 * implemented in {@link AbstractGenericDao}. </p>
 * 
 * <p>For all another classes in {@link tk.serjmusic.dao.impl} only custom methods should
 * be tested.</p>
 *
 * @author Roman Kondakov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
@Transactional
@Rollback
public class UserDaoImplTest {
    
    private static final String USERNAME = "John_Doe";
    private static final String ANOTHER_USERNAME = "Jane_Doe";
    private static final String EMAIL = "john.doe@mail.com";
    private User user = null;
    private BlogEntry blog = null;
    private BlogComment blogComment = null;
            
    @Autowired
    UserDao userDao;
    

    /**
     * Set up method - init database with user entity
     * 
     * @throws java.lang.Exception
     */
    @Commit
    @Before
    public  void setUp() throws Exception {
        try {
            user = userDao.getUserByUsername(USERNAME);
        } catch (NoResultException e) {
            user = new User(USERNAME);
            user.setEmail(EMAIL);
            blog = new BlogEntry("test blog");
            blogComment = new BlogComment("test comment");
            user.setRoles(new HashSet<>(Arrays.asList(UserRole.ROLE_ADMIN)));
            user.setBlogs(new HashSet<>(Arrays.asList(blog)));
            user.setComments(new HashSet<>(Arrays.asList(blogComment)));
            user.setPassword("test");
            System.out.println("user : " + user); 
            userDao.persist(user);
        }
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.UserDaoImpl#getUserByUsername(java.lang.String)}.
     */
    @Test
    @Transactional
    public final void testGetUserByUsername() {
        assertEquals(USERNAME, userDao.getUserByUsername(USERNAME).getUsername());
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.UserDaoImpl#getUserByEmail(java.lang.String)}.
     */
    @Test
    public final void testGetUserByEmail() {
        assertEquals(EMAIL, userDao.getUserByEmail(EMAIL).getEmail());
    }

    /**
     * Test method for {@link tk.serjmusic.dao.impl.UserDaoImpl#getUserCommentsByUserId(int, int, int)}.
     */
    @Test
    public final void testGetUserCommentsByUserId() {
        assertTrue(blogComment
                .equals(userDao.getUserCommentsByUserId(user.getId(), 1, 1).get(0))); 
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#persist(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testPersist() {
        userDao.persist(new User(ANOTHER_USERNAME));
        assertEquals(2, userDao.countAll());
        assertTrue(ANOTHER_USERNAME.equals(userDao.getUserByUsername(ANOTHER_USERNAME).getUsername()));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#find(int)}.
     */
    @Test
    public final void testFind() {
        assertEquals(user, userDao.getUserByUsername(USERNAME));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#merge(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testMerge() {
        user = userDao.getUserByUsername(USERNAME);
        user.setUsername(ANOTHER_USERNAME);
        userDao.merge(user);
        assertNotNull(userDao.getUserByUsername(ANOTHER_USERNAME));
        
        try {
            user = userDao.getUserByUsername(USERNAME);
        } catch (NoResultException e) {
            user = null;
        }
        assertNull(user);
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#remove(tk.serjmusic.models.AbstractEntity)}.
     */
    @Test
    public final void testRemove() {
        boolean userRemovedFromDb = false;
        assertEquals(1, userDao.findAll().size());
        userDao.remove(userDao.getUserByEmail(USERNAME));
        try {
            assertEquals(0, userDao.findAll().size());
        } catch (NoResultException e) {
            userRemovedFromDb = true;
        }
        assertTrue(userRemovedFromDb);
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#findAll()}.
     */
    @Test
    public final void testFindAll() {
        User another = new User(ANOTHER_USERNAME);
        userDao.persist(another);
        List<User> users = userDao.findAll();
        assertEquals(user, users.get(0));
        assertEquals(another, users.get(1));
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#countAll()}.
     */
    @Test
    public final void testCountAll() {
        assertEquals(1, userDao.countAll());
        userDao.persist(new User(ANOTHER_USERNAME));
        assertEquals(2, userDao.countAll());
    }

    /**
     * Test method for {@link tk.serjmusic.dao.AbstractGenericDao#findPaginatedAndOrdered(boolean, int, int)}.
     */
    @Test
    public final void testFindPaginatedAndOrdered() {
        User user2 = new User(ANOTHER_USERNAME);
        User user3 = new User("SomeThirdName");
        userDao.persist(user2);
        userDao.persist(user3);
        List<User> users = userDao.findPaginatedAndOrdered(true, 1, 2);
        assertEquals(2, users.size());
        assertEquals(user, users.get(0));
        assertEquals(user2, users.get(1));
        users = userDao.findPaginatedAndOrdered(true, 2, 2);
        assertEquals(1, users.size());
        assertEquals(user3, users.get(0));
        users = userDao.findPaginatedAndOrdered(false, 1, 1);
        assertEquals(user3, users.get(0));
    }

}
