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

package tk.serjmusic.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tk.serjmusic.dao.UserDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.services.exceptions.AlreadyExistsException;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

/**
 * The main junit test class for service level. Common logic implemented in
 * {@link AbstractGenericServiceImpl} tests here.
 *
 * @author Roman Kondakov
 */

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    // test data declaration
    private static final String USERNAME_1 = "Test_user_1";
    private static final String USERNAME_2 = "Test_user_2";
    private User user1;
    private User user2;
    private BlogEntry blogEntry;
    private BlogComment blogComment1;
    private BlogComment blogComment2;
    private List<BlogComment> comments;
    private List<User> users;

    /**
     * Set up method.
     * 
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // init test data
        user1 = new User(USERNAME_1);
        user2 = new User(USERNAME_2);
        blogComment1 = new BlogComment("test_comment_1");
        blogComment2 = new BlogComment("test_comment_2");
        blogEntry = new BlogEntry("test_blog_entry");
        comments = new ArrayList<>(Arrays.asList(blogComment1, blogComment2));
        blogEntry.setComments(comments);
        users = Arrays.asList(user1, user2);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.UserServiceImpl#getUserByUsername(java.lang.String)}.
     */
    @Test
    public final void testGetUserByUsername() {
        
        // should be OK
        when(userDao.findUserByUsername(USERNAME_1)).thenReturn(user1);
        assertEquals(user1, userService.getUserByUsername(USERNAME_1));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.getUserByUsername(null);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(userDao.findUserByUsername(USERNAME_1)).thenReturn(null);
        try {
            userService.getUserByUsername(USERNAME_1);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.findUserByUsername(USERNAME_1)).thenThrow(new PersistenceException());
        try {
            userService.getUserByUsername(USERNAME_1);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.UserServiceImpl#getUserByEmail(java.lang.String)}.
     */
    @Test
    public final void testGetUserByEmail() {

        String email = "some@email.com";

        // should be OK
        user1.setEmail(email);
        when(userDao.findUserByEmail(email)).thenReturn(user1);
        assertEquals(user1, userService.getUserByEmail(email));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.getUserByEmail(null);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(userDao.findUserByEmail(email)).thenReturn(null);
        try {
            userService.getUserByEmail(email);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.findUserByEmail(email)).thenThrow(new PersistenceException());
        try {
            userService.getUserByEmail(email);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.UserServiceImpl#getUserCommentsByUserId(int, int, int)}.
     */
    @Test
    public final void testGetUserCommentsByUserId() {
        int id = 1;
        int pageNumber = 1;
        int pageSize = 2;
        // should be OK
        when(userDao.findUserCommentsByUserId(id, pageNumber, pageSize)).thenReturn(comments);
        assertEquals(comments, userService.getUserCommentsByUserId(id, pageNumber, pageSize));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.getUserCommentsByUserId(-1, pageNumber, pageSize);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        try {
            userService.getUserCommentsByUserId(id, 0, pageSize);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException &= true;
        }
        try {
            userService.getUserCommentsByUserId(id, pageNumber, -1);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException &= true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(userDao.findUserCommentsByUserId(id, pageNumber, pageSize)).thenReturn(null);
        try {
            userService.getUserCommentsByUserId(id, pageNumber, pageSize);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.findUserCommentsByUserId(id, pageNumber, pageSize))
                .thenThrow(new PersistenceException());
        try {
            userService.getUserCommentsByUserId(id, pageNumber, pageSize);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl
     * #create(tk.serjmusic.models.AbstractEntity)}
     * .
     */
    @Test
    public final void testCreate() {
        
        // should be OK
        doNothing().when(userDao).persist(user1);
        userService.create(user1);
        verify(userDao, times(1)).persist(user1);

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.create(null);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);
        
        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        doThrow(new PersistenceException()).when(userDao).persist(user1);
        try {
            userService.create(user1);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
        
        // Entity already exist
        boolean alreadyExistsException = false;
        doThrow(new EntityExistsException()).when(userDao).persist(user1);
        try {
            userService.create(user1);
        } catch (AlreadyExistsException ex) {
            alreadyExistsException = true;
        }
        assertTrue(alreadyExistsException);
    }

    /**
     * Test method for {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl#getById(int)}.
     */
    @Test
    public final void testGetById() {
        int id = 1;
        // should be OK
        when(userDao.find(id)).thenReturn(user1);
        assertEquals(user1, userService.getById(id));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.getById(-1);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(userDao.find(id)).thenReturn(null);
        try {
            userService.getById(id);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.find(id)).thenThrow(new PersistenceException());
        try {
            userService.getById(id);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl
     * #update(tk.serjmusic.models.AbstractEntity)}
     * .
     */
    @Test
    public final void testUpdate() {
        
        // should be OK
        when(userDao.merge(user1)).thenReturn(user1);
        assertEquals(user1, userService.update(user1));;
        verify(userDao, times(1)).merge(user1);

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.update(null);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);
        
        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        doThrow(new PersistenceException()).when(userDao).merge(user1);
        try {
            userService.update(user1);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl
     * #delete(tk.serjmusic.models.AbstractEntity)}
     * .
     */
    @Test
    public final void testDelete() {
        
        // should be OK
        doNothing().when(userDao).remove(user1);
        userService.delete(user1);
        verify(userDao, times(1)).remove(user1);

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.delete(null);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        assertTrue(illegalArgumentException);
        
        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        doThrow(new PersistenceException()).when(userDao).remove(user1);
        try {
            userService.delete(user1);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl#getAll()}.
     */
    @Test
    public final void testGetAll() {
        
        
        
        // should be OK
        when(userDao.findAll()).thenReturn(users);
        assertEquals(users, userService.getAll());

        // Empty result
        boolean canNotFindException = false;
        when(userDao.findAll()).thenReturn(null);
        try {
            userService.getAll();
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.findAll()).thenThrow(new PersistenceException());
        try {
            userService.getAll();
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl#countAll()}.
     */
    @Test
    public final void testCountAll() {
        int usersQuantity = 10;
        
        // should be OK
        when(userDao.countAll()).thenReturn(usersQuantity);
        assertEquals(usersQuantity, userService.countAll());

        // Empty result
        boolean canNotFindException = false;
        when(userDao.countAll()).thenReturn(0);
        try {
            userService.countAll();
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.countAll()).thenThrow(new PersistenceException());
        try {
            userService.countAll();
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }

    /**
     * Test method for
     * {@link tk.serjmusic.services.impl.AbstractGenericServiceImpl
     * #getPaginatedAndOrdered(boolean, int, int)}
     * .
     */
    @Test
    public final void testGetPaginatedAndOrdered() {
        boolean ascOrderById = true;
        int pageNumber = 1;
        int pageSize = 2;
        // should be OK
        when(userDao.findPaginatedAndOrdered(ascOrderById, pageNumber, pageSize)).thenReturn(users);
        assertEquals(users, userService.getPaginatedAndOrdered(ascOrderById, pageNumber, pageSize));

        // Bad input
        boolean illegalArgumentException = false;
        try {
            userService.getPaginatedAndOrdered(ascOrderById, 0, pageSize);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException = true;
        }
        try {
            userService.getPaginatedAndOrdered(ascOrderById, pageNumber, -1);
        } catch (IllegalArgumentException ex) {
            illegalArgumentException &= true;
        }
        assertTrue(illegalArgumentException);

        // Empty result
        boolean canNotFindException = false;
        when(userDao.findPaginatedAndOrdered(ascOrderById, pageNumber, pageSize)).thenReturn(null);
        try {
            userService.getPaginatedAndOrdered(ascOrderById, pageNumber, pageSize);
        } catch (CanNotFindException ex) {
            canNotFindException = true;
        }
        assertTrue(canNotFindException);

        // Problems with persistent layer
        boolean persistentLayerProblemsException = false;
        when(userDao.findPaginatedAndOrdered(ascOrderById, pageNumber, pageSize))
                .thenThrow(new PersistenceException());
        try {
            userService.getPaginatedAndOrdered(ascOrderById, pageNumber, pageSize);
        } catch (PersistentLayerProblemsException ex) {
            persistentLayerProblemsException = true;
        }
        assertTrue(persistentLayerProblemsException);
    }
    
    

}
