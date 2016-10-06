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

package tk.serjmusic.controllers.dto.asm;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import tk.serjmusic.controllers.UsersController;
import tk.serjmusic.controllers.dto.UserDto;
import tk.serjmusic.models.User;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO assembler for {@link User} entity.
 *
 * @author Roman Kondakov
 */
public class UserDtoAsm extends ResourceAssemblerSupport<User, UserDto> {

    public UserDtoAsm() {
        super(UsersController.class, UserDto.class);
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
     */
    @Override
    public UserDto toResource(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setBanned(user.isBanned());
        userDto.setImageLink(user.getImageLink());
        userDto.setImageFile(user.getImageFile());
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.toString()));
        userDto.setRoles(roles);
        Link self = linkTo(UsersController.class).slash(user.getId()).withSelfRel();
        userDto.add(self);
        Link users = linkTo(UsersController.class).withRel("allUsers");
        userDto.add(users);
        return userDto;
    }

}
