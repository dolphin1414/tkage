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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import tk.serjmusic.dao.UserDao;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogComment_;
import tk.serjmusic.models.User;
import tk.serjmusic.models.User_;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * JPA implementation of {@link UserDao}.
 *
 * @author Roman Kondakov
 */
@Repository
public class UserDaoImpl extends AbstractGenericDao<User> implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    private void init() {
        setEntityManager(entityManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.UserDao#getUserByUsername(java.lang.String)
     */
    @Override
    public User findUserByUsername(String username) {
        User result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> from = cq.from(User.class);
            cq.select(from).where(cb.equal(from.get(User_.username), username));
            TypedQuery<User> tq = entityManager.createQuery(cq);
            result = tq.getSingleResult();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for getUserByUsername " + username, ex);
            }
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.UserDao#getUserByEmail(java.lang.String)
     */
    @Override
    public User findUserByEmail(String email) {
        User result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> from = cq.from(User.class);
            cq.select(from).where(cb.equal(from.get(User_.email), email));
            TypedQuery<User> tq = entityManager.createQuery(cq);
            result = tq.getSingleResult();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for findUserByEmail " + email, ex);
            }
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.UserDao#getUserCommentsByUserId(int, int, int)
     */
    @Override
    public List<BlogComment> findUserCommentsByUserId(int id, int pageNumber, int pageSize) {
        List<BlogComment> result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<BlogComment> cq = cb.createQuery(BlogComment.class);
            Root<BlogComment> blogCommentRoot = cq.from(BlogComment.class);
            Join<BlogComment, User> u = blogCommentRoot.join(BlogComment_.author);
            cq.select(blogCommentRoot).where(cb.equal(u.get(User_.id), id));
            cq.orderBy(cb.asc(blogCommentRoot.get(BlogComment_.id)));
            cq.distinct(true);
            TypedQuery<BlogComment> tq = entityManager.createQuery(cq);
            int startPosition = (pageNumber - 1) * pageSize;
            tq.setFirstResult(startPosition).setMaxResults(pageSize);
            result = tq.getResultList();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for findUserCommentsByUserId, id: " + id + "; pageNumber:"
                        + pageNumber + "; pageSize: " + pageSize, ex);
            }
            result = null;
        }
        return result;
    }
}
