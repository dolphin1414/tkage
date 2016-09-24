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

package tk.serjmusic.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * User class for web application. It's an implementation of {@link UserDetails}
 * interface.
 *
 * @author Roman Kondakov
 */

@Entity
@Table(name = "users")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends AbstractEntity implements UserDetails {
    
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "username should contain letters and numbers only")
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String username;
    
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String password = "";
    
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email = "";
    
    @ElementCollection(targetClass = tk.serjmusic.models.UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<UserRole> roles = new HashSet<>();
    
    @Column(name = "is_banned", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isBanned;
    
    @Column(name = "image_link", columnDefinition = "TEXT")
    private String imageLink;
    
    @Lob
    @Column(name = "image_file")
    private byte[] imageFile;
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BlogEntry> blogs = new HashSet<>();
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BlogComment> comments = new HashSet<>();
    
    public User() {
        
    }
    
    public User(String username) {
        //Testing purposes
        this.username = username;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return !isBanned;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isBanned;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !isBanned;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return !isBanned;
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
    public Set<UserRole> getRoles() {
        return roles;
    }

    /**
     * Roles setter.
     * 
     * @param roles the roles to set
     */
    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    /**
     * Whether user is banned.
     * 
     * @return the isBanned
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * Set user ban status.
     * 
     * @param isBanned the isBanned to set
     */
    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
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
     * Password setter.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Blogs getter.
     * 
     * @return the blogs
     */
    public Set<BlogEntry> getBlogs() {
        return blogs;
    }

    /**
     * Blogs setter.
     * 
     * @param blogs the blogs to set
     */
    public void setBlogs(Set<BlogEntry> blogs) {
        this.blogs = blogs;
    }

    /**
     * Comments getter.
     * 
     * @return the comments
     */
    public Set<BlogComment> getComments() {
        return comments;
    }

    /**
     * Comments setter.
     * 
     * @param comments the comments to set
     */
    public void setComments(Set<BlogComment> comments) {
        this.comments = comments;
    }
    

    /**
     * Image link setter.
     * 
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * Image link getter.
     * 
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * Image file setter.
     * 
     * @return the imageFile
     */
    public byte[] getImageFile() {
        return imageFile;
    }

    /**
     * Image file getter.
     * 
     * @param imageFile the imageFile to set
     */
    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
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
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
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
        return "#" + getId() + ", User [username=" + username + ", password=" + password 
                + ", email=" + email + ", roles=" + roles + ", isBanned=" + isBanned + "]";
    }
}
