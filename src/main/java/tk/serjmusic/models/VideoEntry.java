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
 * A video entry entity for a blog. Contains a video description and a link to YouTube.
 * 
 * @author Roman Kondakov
 */

@Entity
@Table(name = "video_entries")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoEntry extends AbstractEntity {

    @Column(name = "title", nullable = false, columnDefinition = "TINYTEXT")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description = "";

    @Column(name = "youtube_link", nullable = false, columnDefinition = "TEXT")
    private String youTubeLink = "";

    public VideoEntry() {

    }

    public VideoEntry(String title) {
        this.title = title;
    }

    /**
     * Video entry title getter.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Video entry title setter.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Video entry description getter.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Video entry description setter.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Video entry YouTube link getter.
     * 
     * @return the youTubeLink
     */
    public String getYouTubeLink() {
        return youTubeLink;
    }

    /**
     * Video entry YouTube link setter.
     * 
     * @param youTubeLink the youTubeLink to set
     */
    public void setYouTubeLink(String youTubeLink) {
        this.youTubeLink = youTubeLink;
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
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((youTubeLink == null) ? 0 : youTubeLink.hashCode());
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
        if (!(obj instanceof VideoEntry)) {
            return false;
        }
        VideoEntry other = (VideoEntry) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (youTubeLink == null) {
            if (other.youTubeLink != null) {
                return false;
            }
        } else if (!youTubeLink.equals(other.youTubeLink)) {
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
        return "#" + getId() + ", VideoEntry [title=" + title + ", description=" + description + ", youTubeLink="
                + youTubeLink + "]";
    }
}
