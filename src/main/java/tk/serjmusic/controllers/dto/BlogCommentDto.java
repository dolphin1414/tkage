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

import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;

import java.util.Date;


/**
 * The DTO for {@link BlogComment} entity.
 *
 * @author Roman Kondakov
 */
public class BlogCommentDto extends ResourceSupport {
    
    private int commentId;
    private String content;
    private Date dateCreated;
    private UserDto author;
    private BlogEntryDto blogEntry;
    
    /**
     * Overwrite non null fields of JPA entity with an information from DTO.
     * 
     * @param blogComment - entity to be overwritten
     * @return overwritten entity
     */
    public BlogComment overwriteEntity(BlogComment blogComment) {
        if (commentId > 0) {
            blogComment.setId(commentId);
        }
        if (content != null) {
            blogComment.setContent(content);
        }
        if (dateCreated != null) {
            blogComment.setDateCreated(dateCreated);
        }
        if (author != null) {
            blogComment.setAuthor(author.overwriteEntity(new User()));
        }
        if (blogEntry != null) {
            blogComment.setBlogEntry(
                    blogEntry.overwriteEntity(new BlogEntry()));
        }
        return blogComment;
    }
    
    /**
     * Getter for BlogCommentDto commentId.
     *
     * @return the commentId
     */
    public int getCommentId() {
        return commentId;
    }
    
    /**
     * Setter for BlogCommentDto commentId.
     *
     * @param commentId the commentId to set
     */
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    
    /**
     * Getter for BlogCommentDto content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Setter for BlogCommentDto content.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Getter for BlogCommentDto dateCreated.
     *
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }
    
    /**
     * Setter for BlogCommentDto dateCreated.
     *
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    /**
     * Getter for BlogCommentDto author.
     *
     * @return the author
     */
    public UserDto getAuthor() {
        return author;
    }
    
    /**
     * Setter for BlogCommentDto author.
     *
     * @param author the author to set
     */
    public void setAuthor(UserDto author) {
        this.author = author;
    }
    
    /**
     * Getter for BlogCommentDto blogEntry.
     *
     * @return the blogEntry
     */
    public BlogEntryDto getBlogEntry() {
        return blogEntry;
    }
    
    /**
     * Setter for BlogCommentDto blogEntry.
     *
     * @param blogEntry the blogEntry to set
     */
    public void setBlogEntry(BlogEntryDto blogEntry) {
        this.blogEntry = blogEntry;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((blogEntry == null) ? 0 : blogEntry.hashCode());
        result = prime * result + commentId;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
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
        if (!(obj instanceof BlogCommentDto)) {
            return false;
        }
        BlogCommentDto other = (BlogCommentDto) obj;
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (blogEntry == null) {
            if (other.blogEntry != null) {
                return false;
            }
        } else if (!blogEntry.equals(other.blogEntry)) {
            return false;
        }
        if (commentId != other.commentId) {
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
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BlogCommentDto [commentId=" + commentId + ", content=" + content + ", dateCreated="
                + dateCreated + ", author=" + author + ", blogEntry=" + blogEntry + "]";
    }
}
