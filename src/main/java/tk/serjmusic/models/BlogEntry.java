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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A blog entity. Contains blog title, description and link to blog images. Blog entries may be
 * associated with it's comments.
 *
 * @author Roman Kondakov
 */

@Entity
@Table(name = "blog_entries")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BlogEntry extends AbstractEntity {

    @Column(name = "title", nullable = false, columnDefinition = "TINYTEXT")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_link", columnDefinition = "TEXT")
    private String imageLink;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    
    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<BlogComment> comments = new ArrayList<>();

    public BlogEntry() {
        
    }
    
    public BlogEntry(String title) {
        this.title = title;
    }

    /**
     * Title getter.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Title setter.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * Image link getter.
     * 
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * Image link setter.
     * 
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * Creation date getter.
     * 
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Creation date setter.
     * 
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Author getter.
     * 
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Author setter.
     * 
     * @param author the author to set
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Comments getter.
     * 
     * @return the comments
     */
    public List<BlogComment> getComments() {
        return comments;
    }

    /**
     * Comments setter.
     * 
     * @param comments the comments to set
     */
    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
        result = prime * result + ((imageLink == null) ? 0 : imageLink.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        if (!(obj instanceof BlogEntry)) {
            return false;
        }
        BlogEntry other = (BlogEntry) obj;
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (dateCreated == null) {
            if (other.dateCreated != null) {
                return false;
            }
        } else if (!dateCreated.equals(other.dateCreated)) {
            return false;
        }
        if (imageLink == null) {
            if (other.imageLink != null) {
                return false;
            }
        } else if (!imageLink.equals(other.imageLink)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "#" + getId() + ", BlogEntry [title=" + title + ", content=" + content 
                + ", imageLink=" + imageLink + ", dateCreated=" + dateCreated 
                + ", author=" + author + "]";
    }
}
