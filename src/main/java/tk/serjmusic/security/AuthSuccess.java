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

package tk.serjmusic.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import tk.serjmusic.controllers.dto.UserDto;
import tk.serjmusic.models.User;
import tk.serjmusic.utils.logging.Loggable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An implemetation of {@link SimpleUrlAuthenticationSuccessHandler} interface.
 *
 * @author Roman Kondakov
 */
@Component("authSuccess")
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler {
    
    private final static ObjectWriter objectWriter = 
            new ObjectMapper().writer().withDefaultPrettyPrinter();

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    @Loggable
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setImageLink(user.getImageLink());
            Set<String> roles = new HashSet<>();
            user.getRoles().forEach(role -> roles.add(role.toString()));
            userDto.setRoles(roles);
            String userJson = objectWriter.writeValueAsString(userDto);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(userJson);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
