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

package tk.serjmusic.controllers.dto;

import org.springframework.hateoas.ResourceSupport;

import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.utils.logging.Loggable;

import java.util.Date;


/**
 * The DTO for {@link BlogEntry} entity.
 *
 * @author Roman Kondakov
 */
public class BlogEntryDto extends ResourceSupport {
    
    private int blogId;
    private String title;
    private String content;
    private String imageLink;
    private Date dateCreated;
    private UserDto author;
    
    /**
     * Overwrite non null fields of JPA entity with an information from DTO.
     * 
     * @param blogEntry - entity to be overwritten
     * @return overwritten entity
     */
    @Loggable
    public BlogEntry overwriteEntity(BlogEntry blogEntry) {
        System.out.println("blogEntry: " + blogEntry);
        if (blogId > 0) blogEntry.setId(blogId);
        if (title != null) blogEntry.setTitle(title);
        if (content != null) blogEntry.setContent(content);
        if (imageLink != null) blogEntry.setImageLink(imageLink);
        if (dateCreated != null) blogEntry.setDateCreated(dateCreated);
        if (author != null) blogEntry.setAuthor(author.overwriteEntity(new User()));
        return blogEntry;
    }
    
    /**
     * Blog ID getter.
     * 
     * @return the blogId
     */
    public int getBlogId() {
        return blogId;
    }
    
    /**
     * Blog ID setter.
     * 
     * @param blogId the blogId to set
     */
    public void setBlogId(int blogId) {
        this.blogId = blogId;
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
     * Blog content getter.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Blog content setter.
     * 
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Blog image link getter.
     * 
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }
    
    /**
     * Blog image link setter.
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
    public UserDto getAuthor() {
        return author;
    }
    
    /**
     * Author setter.
     * 
     * @param author the author to set
     */
    public void setAuthor(UserDto author) {
        this.author = author;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + blogId;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
        result = prime * result + ((imageLink == null) ? 0 : imageLink.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof BlogEntryDto)) {
            return false;
        }
        BlogEntryDto other = (BlogEntryDto) obj;
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (blogId != other.blogId) {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BlogEntryDto [blogId=" + blogId + ", title=" + title + ", content=" + content
                + ", imageLink=" + imageLink + ", dateCreated=" + dateCreated 
                + ", author=" + author + "]";
    }
}
