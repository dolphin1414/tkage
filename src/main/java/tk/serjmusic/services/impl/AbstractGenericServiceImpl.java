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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.serjmusic.dao.GenericDao;
import tk.serjmusic.models.AbstractEntity;
import tk.serjmusic.services.GenericService;
import tk.serjmusic.services.exceptions.AlreadyExistsException;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;
import tk.serjmusic.utils.logging.Loggable;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

/**
 * A JPA implementation of {@link GenericService}.
 *
 * @author Roman Kondakov
 */

@Service
@Transactional
public abstract class AbstractGenericServiceImpl<T extends AbstractEntity>
        implements GenericService<T> {
    
    /*
     * All methods checks it's input parameters and if it is incorrect the 
     * {@link IllegalArgumentException} will be thrown.
     * 
     * All methods intercept {@link PersistenceException} and throw 
     * {@link PersistentLayerProblemsException} when it occurs;
     */

    @Autowired
    private GenericDao<T> dao;

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#create(java.lang.Object)
     */
    @Loggable
    @Override
    public T create(T t) {
        if (t == null) {
            throw new IllegalArgumentException("entity is null");
        }
        try {
            return dao.merge(t);
        } catch (EntityExistsException ex) {
            throw new AlreadyExistsException("entity: " + t, ex);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("entity: " + t, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#getById(int)
     */
    @Loggable
    @Override
    public T getById(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("bad id=" + id);
        }
        T result;
        try {
            result = dao.find(id);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("id: " + id, ex);
        }
        if (result == null) {
            throw new CanNotFindException("Can not find entity for id=" + id);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#update(java.lang.Object)
     */
    @Loggable
    @Override
    public T update(T t) {
        if (t == null) {
            throw new IllegalArgumentException("entity is null");
        }
        T result;
        try {
            result = dao.merge(t);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("entity: " + t, ex);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#delete(java.lang.Object)
     */
    @Loggable
    @Override
    public void delete(T t) {
        if (t == null) {
            throw new IllegalArgumentException("entity is null");
        }
        try {
            dao.remove(t);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("entity: " + t, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#getAll()
     */
    @Loggable
    @Override
    public List<T> getAll() {
        List<T> result = null;
        try {
            result = dao.findAll();
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("no result ", ex);
        }
        if (result == null) {
            throw new CanNotFindException("Can not find entities");
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#countAll()
     */
    @Loggable
    @Override
    public int countAll() {
        int result = 0;
        try {
            result = dao.countAll();
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("no result ", ex);
        }
        if (result == 0) {
            throw new CanNotFindException("Can not find entities");
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see tk.serjmusic.services.GenericService#getPaginatedAndOrdered(boolean, int, int)
     */
    @Loggable
    @Override
    public List<T> getPaginatedAndOrdered(boolean ascOrderById, int pageNumber, int pageSize) {
        if ((pageNumber < 1) || (pageSize < 0)) {
            throw new IllegalArgumentException("bad argument(s) : pageNumber=" + pageNumber 
                    + "; pageSize=" + pageSize);
        }
        List<T> result = null;
        try {
            result = dao.findPaginatedAndOrdered(ascOrderById, pageNumber, pageSize);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("no result ", ex);
        }
        if (result == null) {
            throw new CanNotFindException("Can not find entities");
        }
        return result;
    }
}
