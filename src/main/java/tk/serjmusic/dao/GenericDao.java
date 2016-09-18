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

package tk.serjmusic.dao;

import tk.serjmusic.models.AbstractEntity;

import java.util.List;

/**
 * Generic interface for all DAO classes in project. All it's functionality is already
 * implemented in {@link AbstractGenericDao} abstract class, so it's not necessary to 
 * implement this logic for each entity class separately - it's enough to create subclass of
 * this abstract class.
 *
 * @author Roman Kondakov
 */
public interface GenericDao<T extends AbstractEntity> {
    
    /**
     * Persist entity in a persistence context.
     * 
     * @param t - entity instance
     */
    public void persist(T t);
    
    /**
     * Get entity from persistence context by id.
     * 
     * @param id the id of entity
     * @return the entity with requested id
     */
    public T find(int id);
    
    /**
     * Merge (update) entity in the persistence context.
     * 
     * @param t entity
     * @return updated entity
     */
    public T merge(T t);

    /**
     * Removes entity from the persistence context.
     * 
     * @param t entity to be removed
     */
    public void remove(T t);
    
    /**
     * Retrieve all entities of specified type from the persistence context.
     * 
     * @return list of entities
     */
    public List<T> findAll();
    
    /**
     * Count a number of entities of specified type in the persistence context.
     * 
     * @return number of entities
     */
    public int countAll();
    
    /**
     * Retrieve up to {@code maxResults} entities beginning with {@code firstResult}
     * from the all entities which fit to given criteria.
     * 
     * @param ascOrderById whether the ascend ID order should be used for result retrieving
     * @param pageNumber number of page to be returned (pages starts with 1)
     * @param pageSize size of data page
     * @return list of entities fit to criteria
     */
    public List<T> findPaginatedAndOrdered(boolean ascOrderById, int pageNumber, int pageSize);
    
    
}
