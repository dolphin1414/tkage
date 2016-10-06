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

import tk.serjmusic.models.StaticContent;

/**
 * The DTO for {@link StaticContent} entity.
 *
 * @author Roman Kondakov
 */
public class StaticContentDto extends ResourceSupport {
    
    private int staticContentId;
    private String contentDescription;
    private String language;
    private String content;
    
    /**
     * Overwrite non null fields of JPA entity with an information from DTO.
     * 
     * @param staticContent - entity to be overwritten
     * @return overwritten entity
     */
    public StaticContent overwriteEntity(StaticContent staticContent) {
        if (staticContentId > 0) {
            staticContent.setId(staticContentId);
        }
        if (contentDescription != null) {
            staticContent.setContentDescription(contentDescription);
        }
        if (language != null) {
            staticContent.setLanguage(language);
        }
        if (content != null) {
            staticContent.setContent(content);
        }
        return staticContent;
    }
    
    /**
     * Getter for StaticContentDto staticContentId.
     *
     * @return the staticContentId
     */
    public int getStaticContentId() {
        return staticContentId;
    }

    /**
     * Setter for StaticContentDto staticContentId.
     *
     * @param staticContentId the staticContentId to set
     */
    public void setStaticContentId(int staticContentId) {
        this.staticContentId = staticContentId;
    }

    /**
     * Getter for StaticContentDto contentDescription.
     *
     * @return the contentDescription
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * Setter for StaticContentDto contentDescription.
     *
     * @param contentDescription the contentDescription to set
     */
    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    /**
     * Getter for StaticContentDto language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Setter for StaticContentDto language.
     *
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Getter for StaticContentDto content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter for StaticContentDto content.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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
        result = prime * result + staticContentId;
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
        if (!(obj instanceof StaticContentDto)) {
            return false;
        }
        StaticContentDto other = (StaticContentDto) obj;
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
        if (staticContentId != other.staticContentId) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StaticContentDto [staticContentId=" + staticContentId + ", contentDescription="
                + contentDescription + ", language=" + language + ", content=" + content + "]";
    }
}
