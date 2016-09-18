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

package tk.serjmusic.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An entiity for static website content {@literal (i.e. Contacts, About etc.)}.
 *
 * @author Roman Kondakov
 */
@Entity
@Table(name = "static_content")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaticContent extends AbstractEntity {

    @Column(name = "content_description", nullable = false, columnDefinition = "TINYTEXT")
    private String contentDescription;

    @Column(name = "content_language", nullable = false, columnDefinition = "CHAR(2)")
    private String language = "RU";

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    public StaticContent() {
        
    }
    
    public StaticContent(String content) {
        //For testing purposes
        this.contentDescription = content;
        this.content = content;
    }

    /**
     * Static content description getter.
     * 
     * @return the contentDescription
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * Static content description getter.
     * 
     * @param contentDescription the contentDescription to set
     */
    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    /**
     * Static content language (two letters {@literal e.g.} "RU" or "EN") getter.
     * 
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Static content language (two letters {@literal e.g.} "RU" or "EN") setter.
     * 
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Content getter.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Content setter.
     * 
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StaticContent [contentDescription=" + contentDescription + ", language=" + language
                + ", content=" + content + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result
                + ((contentDescription == null) ? 0 : contentDescription.hashCode());
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StaticContent)) {
            return false;
        }
        StaticContent other = (StaticContent) obj;
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (contentDescription == null) {
            if (other.contentDescription != null) {
                return false;
            }
        } else if (!contentDescription.equals(other.contentDescription)) {
            return false;
        }
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        return true;
    }
}
