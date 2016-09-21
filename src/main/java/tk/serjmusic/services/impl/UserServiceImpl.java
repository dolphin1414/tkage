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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.serjmusic.dao.UserDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.User;
import tk.serjmusic.services.UserService;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;
import tk.serjmusic.utils.logging.Loggable;

import java.util.List;

import javax.persistence.PersistenceException;

/**
 * An implementation of {@link UserService}. Most of basic logic is implemented in the
 * {@link AbstractGenericServiceImpl}.
 *
 * @author Roman Kondakov
 */

@Service
@Transactional
public class UserServiceImpl extends AbstractGenericServiceImpl<User> 
        implements UserService {
    
    /*
     * All methods checks it's input parameters and if it is incorrect the 
     * {@link IllegalArgumentException} will be thrown.
     * 
     * All methods intercept {@link PersistenceException} and throw 
     * {@link PersistentLayerProblemsException} when it occurs;
     */

    @Autowired
    private UserDao userDao;
    
    /* (non-Javadoc)
     * @see tk.serjmusic.services.UserService#getUserByUsername(java.lang.String)
     */
    @Loggable
    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username is null");
        }
        User result = null;
        try {
            result = userDao.findUserByUsername(username);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("username: " + username, ex);
        }
        if (result == null) {
            throw new CanNotFindException("DAO " + userDao.getClass().getSimpleName() 
                    + " can not find entity for username=" + username);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see tk.serjmusic.services.UserService#getUserByEmail(java.lang.String)
     */
    @Loggable
    @Override
    public User getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }
        User result = null;
        try {
            result = userDao.findUserByEmail(email);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("email: " + email, ex);
        }
        if (result == null) {
            throw new CanNotFindException("DAO " + userDao.getClass().getSimpleName() 
                    + " can not find entity for email=" + email);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see tk.serjmusic.services.UserService#getUserCommentsByUserId(int, int, int)
     */
    @Loggable
    @Override
    public List<BlogComment> getUserCommentsByUserId(int id, int pageNumber, int pageSize) {
        if ((id < 0) || (pageNumber < 1) || (pageSize < 0)) {
            throw new IllegalArgumentException("bad argument(s) : id=" + id + ";  pageNumber=" 
                    + pageNumber  + "; pageSize=" + pageSize);
        }
        List<BlogComment> result = null;
        try {
            result = userDao.findUserCommentsByUserId(id, pageNumber, pageSize);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("no result ", ex);
        }
        if (result == null) {
            throw new CanNotFindException(
                    "DAO " + userDao.getClass().getSimpleName() + " can not find entities");
        }
        return result;
    }
}
