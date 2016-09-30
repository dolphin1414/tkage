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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import tk.serjmusic.dao.StaticContentDao;
import tk.serjmusic.models.StaticContent;
import tk.serjmusic.models.StaticContent_;
import tk.serjmusic.utils.R;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * JPA implementation of {@link StaticContentDao}.
 *
 * @author Roman Kondakov
 */
@Repository
public class StaticContentDaoImpl extends AbstractGenericDao<StaticContent>
        implements StaticContentDao {
    
    private static final Logger logger = Logger.getLogger(StaticContentDaoImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PostConstruct
    private void init() {
        setEntityManager(entityManager);
    }

    /* (non-Javadoc)
     * @see tk.serjmusic.dao.StaticContentDao#findUserByUsername(java.lang.String)
     */
    @Override
    public StaticContent findStaticContentByDescription(String description) {
        StaticContent result = null;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<StaticContent> cq = cb.createQuery(StaticContent.class);
            Root<StaticContent> from = cq.from(StaticContent.class);
            cq.select(from)
                    .where(cb.equal(from.get(StaticContent_.contentDescription), description));
            TypedQuery<StaticContent> tq = entityManager.createQuery(cq);
            tq.setHint(R.HIBERNATE_QUERY_CACHE_NAME, true);
            result = tq.getSingleResult();
        } catch (NoResultException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results for getUserByUsername " + description, ex);
            }
            result = null;
        }
        return result;
    }
}
