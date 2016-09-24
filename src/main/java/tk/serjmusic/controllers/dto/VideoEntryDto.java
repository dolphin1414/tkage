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

package tk.serjmusic.controllers.dto;

import org.springframework.hateoas.ResourceSupport;

import tk.serjmusic.models.VideoEntry;

/**
 * The DTO for {@link VideoEntry} entity.
 *
 * @author Roman Kondakov
 */
public class VideoEntryDto extends ResourceSupport {

    private int videoEntryId;
    private String title;
    private String description;
    private String youTubeLink;

    /**
     * Getter for VideoEntryDto videoEntryId.
     *
     * @return the videoEntryId
     */
    public int getVideoEntryId() {
        return videoEntryId;
    }

    /**
     * Setter for VideoEntryDto videoEntryId.
     *
     * @param videoEntryId the videoEntryId to set
     */
    public void setVideoEntryId(int videoEntryId) {
        this.videoEntryId = videoEntryId;
    }

    /**
     * Getter for VideoEntryDto title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for VideoEntryDto title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for VideoEntryDto description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for VideoEntryDto description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for VideoEntryDto youTubeLink.
     *
     * @return the youTubeLink
     */
    public String getYouTubeLink() {
        return youTubeLink;
    }

    /**
     * Setter for VideoEntryDto youTubeLink.
     *
     * @param youTubeLink the youTubeLink to set
     */
    public void setYouTubeLink(String youTubeLink) {
        this.youTubeLink = youTubeLink;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + videoEntryId;
        result = prime * result + ((youTubeLink == null) ? 0 : youTubeLink.hashCode());
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
        if (!(obj instanceof VideoEntryDto)) {
            return false;
        }
        VideoEntryDto other = (VideoEntryDto) obj;
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
        if (videoEntryId != other.videoEntryId) {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VideoEntryDto [videoEntryId=" + videoEntryId + ", title=" + title + ", description="
                + description + ", youTubeLink=" + youTubeLink + "]";
    }
    
    



}
