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

import tk.serjmusic.dao.StaticContentDao;
import tk.serjmusic.models.StaticContent;
import tk.serjmusic.services.StaticContentService;
import tk.serjmusic.services.exceptions.CanNotFindException;
import tk.serjmusic.services.exceptions.PersistentLayerProblemsException;

import javax.persistence.PersistenceException;

/**
 * An implementation of {@link StaticContentService}. Most of basic logic is implemented in the
 * {@link AbstractGenericServiceImpl}.
 *
 * @author Roman Kondakov
 */

@Service
@Transactional
public class StaticContentServiceImpl extends AbstractGenericServiceImpl<StaticContent> 
        implements StaticContentService{
    
    @Autowired
    private StaticContentDao staticContentDao;

    /* (non-Javadoc)
     * @see tk.serjmusic.services.StaticContentService#getStaticContentByDescription(java.lang.String)
     */
    @Override
    public StaticContent getStaticContentByDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description is null");
        }
        StaticContent result = null;
        try {
            result = staticContentDao.findStaticContentByDescription(description);
        } catch (PersistenceException ex) {
            throw new PersistentLayerProblemsException("description: " + description, ex);
        }
        if (result == null) {
            throw new CanNotFindException("DAO  can not find entity for username=" + description);
        }
        return result;
    }
}
