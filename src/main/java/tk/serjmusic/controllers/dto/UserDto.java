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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

import tk.serjmusic.models.User;

import java.util.Set;


/**
 * The DTO for {@link User} entity.
 *
 * @author Roman Kondakov
 */
public class UserDto extends ResourceSupport {

    private int userId;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
    private boolean isBanned;
    
    /**
     * Id getter.
     * 
     * @return the id
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Id setter.
     * 
     * @param id the id to set
     */
    public void setUserId(int id) {
        this.userId = id;
    }
    
    /**
     * Username getter.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Username setter.
     * 
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Password getter.
     * 
     * @return the password
     */
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    
    /**
     * Password setter.
     * 
     * @param password the password to set
     */
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Email getter.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Email setter.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Roles getter.
     * 
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }
    
    /**
     * Roles setter.
     * 
     * @param roles the roles to set
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    /**
     * Ban status getter.
     * 
     * @return the isBanned
     */
    public boolean isBanned() {
        return isBanned;
    }
    
    /**
     * Ban status setter.
     * 
     * @param isBanned the isBanned to set
     */
    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + userId;
        result = prime * result + (isBanned ? 1231 : 1237);
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        if (!(obj instanceof UserDto)) {
            return false;
        }
        UserDto other = (UserDto) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (userId != other.userId) {
            return false;
        }
        if (isBanned != other.isBanned) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (roles == null) {
            if (other.roles != null) {
                return false;
            }
        } else if (!roles.equals(other.roles)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserDto [id=" + userId + ", username=" + username + ", password=" + password
                + ", email=" + email + ", roles=" + roles + ", isBanned=" + isBanned + "]";
    }
}
