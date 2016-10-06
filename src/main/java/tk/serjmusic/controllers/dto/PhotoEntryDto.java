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

import tk.serjmusic.models.PhotoEntry;


/**
 * The DTO for {@link PhotoEntry} entity.
 *
 * @author Roman Kondakov
 */
public class PhotoEntryDto extends ResourceSupport {
    
    private int photoEntryId;
    private String title;
    private String description;
    private String imageLink;
    private boolean isBackgroundImage;
    
    /**
     * Overwrite non null fields of JPA entity with an information from DTO.
     * 
     * @param photoEntry - entity to be overwritten
     * @return overwritten entity
     */
    public PhotoEntry overwriteEntity(PhotoEntry photoEntry) {
        if (photoEntryId > 0) {
            photoEntry.setId(photoEntryId);
        }
        if (title != null) {
            photoEntry.setTitle(title);
        }
        if (description != null) {
            photoEntry.setDescription(description);
        }
        if (imageLink != null) {
            photoEntry.setImageLink(imageLink);
        }
        photoEntry.setBackgroundImage(isBackgroundImage);
        return photoEntry;
    }
    
    /**
     * Getter for PhotoEntryDto photoEntryId.
     *
     * @return the photoEntryId
     */
    public int getPhotoEntryId() {
        return photoEntryId;
    }
    
    /**
     * Setter for PhotoEntryDto photoEntryId.
     *
     * @param photoEntryId the photoEntryId to set
     */
    
    public void setPhotoEntryId(int photoEntryId) {
        this.photoEntryId = photoEntryId;
    }
    
    /**
     * Getter for PhotoEntryDto title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Setter for PhotoEntryDto title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Getter for PhotoEntryDto description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter for PhotoEntryDto description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for PhotoEntryDto imageLink.
     *
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }
    
    /**
     * Setter for PhotoEntryDto imageLink.
     *
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    
    /**
     * Getter for PhotoEntryDto isBackgroundImage.
     *
     * @return the isBackgroundImage
     */
    public boolean isBackgroundImage() {
        return isBackgroundImage;
    }
    
    /**
     * Setter for PhotoEntryDto isBackgroundImage.
     *
     * @param isBackgroundImage the isBackgroundImage to set
     */
    public void setBackgroundImage(boolean isBackgroundImage) {
        this.isBackgroundImage = isBackgroundImage;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((imageLink == null) ? 0 : imageLink.hashCode());
        result = prime * result + (isBackgroundImage ? 1231 : 1237);
        result = prime * result + photoEntryId;
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
        if (!(obj instanceof PhotoEntryDto)) {
            return false;
        }
        PhotoEntryDto other = (PhotoEntryDto) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (imageLink == null) {
            if (other.imageLink != null) {
                return false;
            }
        } else if (!imageLink.equals(other.imageLink)) {
            return false;
        }
        if (isBackgroundImage != other.isBackgroundImage) {
            return false;
        }
        if (photoEntryId != other.photoEntryId) {
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
        return "PhotoEntryDto [photoEntryId=" + photoEntryId + ", title=" + title + ", description="
                + description + ", imageLink=" + imageLink + ", isBackgroundImage="
                + isBackgroundImage + "]";
    }
}
