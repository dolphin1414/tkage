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

import tk.serjmusic.dao.GenericDao;
import tk.serjmusic.models.AbstractEntity;
import tk.serjmusic.models.AbstractEntity_;
import tk.serjmusic.utils.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * A JPA implementation of {@link GenericDao}.
 *
 * @author Roman Kondakov
 */
public abstract class AbstractGenericDao<T extends AbstractEntity> implements GenericDao<T> {

    private EntityManager entityManager;
    protected final Class<T> genericType;
    private static final Logger logger = Logger.getLogger(AbstractGenericDao.class);
    
    /**
     * Constructor resolves a generic type of DAO subclass.
     */
    @SuppressWarnings("unchecked")
    public AbstractGenericDao() {
        // Set generic type
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        this.genericType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
    
    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#persist(java.lang.Object)
     */
    @Override
    public void persist(T t) {
        entityManager.persist(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#find(int)
     */
    @Override
    public T find(int id) {
        T result = null;
        try {
            result = entityManager.find(genericType, id);
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for find " + genericType + "id: " + id, ex);
            }
            result = null;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#merge(java.lang.Object)
     */
    @Override
    public T merge(T t) {
        return entityManager.merge(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#remove(java.lang.Object)
     */
    @Override
    public void remove(T t) {
        entityManager.remove(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#findAll()
     */
    @Override
    public List<T> findAll() {
        List<T> result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(genericType);
            Root<T> from = cq.from(genericType);
            cq.select(from);
            TypedQuery<T> typedQuery = entityManager.createQuery(cq);
            typedQuery.setHint(R.HIBERNATE_QUERY_CACHE_NAME, true);
            result = typedQuery.getResultList(); 
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for findAll " + genericType , ex);
            }
            result = null;
        }
        return result;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.dao.GenericDao#countAll()
     */
    @Override
    public int countAll() {
        int result;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> from = cq.from(genericType);
            cq.select(cb.count(from));
            TypedQuery<Long> typedQuery = entityManager.createQuery(cq);
            typedQuery.setHint(R.HIBERNATE_QUERY_CACHE_NAME, true);
            result = (int) (long) typedQuery.getSingleResult();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for countAll " + genericType, ex);
            }
            result = 0;
        }
        return result;
        
    }

    /* (non-Javadoc)
     * @see tk.serjmusic.dao.GenericDao#findPaginatedAndOrdered(boolean, int, int)
     */
    @Override
    public List<T> findPaginatedAndOrdered(boolean ascOrderById, int pageNumber, int pageSize) {
        List<T> result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(genericType);
            Root<T> from = cq.from(genericType);
            Order order = ascOrderById ? cb.asc(from.get(AbstractEntity_.id)) 
                                       : cb.desc(from.get(AbstractEntity_.id));
            cq.orderBy(order);
            TypedQuery<T> typedQuery = entityManager.createQuery(cq);
            int startPosition = (pageNumber - 1) * pageSize;
            typedQuery.setFirstResult(startPosition).setMaxResults(pageSize);
            result =  typedQuery.getResultList();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for findPaginatedAndOrdered " + genericType 
                        + "; Id asc order: " + ascOrderById + "; page number: " + pageNumber
                        + "; page size: " + pageSize, ex);
            }
            result = null;
        }
        return result;
    }
}
