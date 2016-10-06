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

package tk.serjmusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tk.serjmusic.controllers.dto.BlogCommentDto;
import tk.serjmusic.controllers.dto.UserDto;
import tk.serjmusic.controllers.dto.asm.BlogCommentDtoAsm;
import tk.serjmusic.controllers.dto.asm.UserDtoAsm;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.User;
import tk.serjmusic.models.UserRole;
import tk.serjmusic.services.UserService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link User} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/users")
public class UsersController {

    private static final UserDtoAsm userDtoAsm = new UserDtoAsm();

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get {@link ResponseEntity} with the paginated list of {@link User} entities. For retrieving
     * all user entities please set {@code pageNumber = 1} and {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link User}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getPaginatedUsers(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<User> users = userService.getPaginatedAndOrdered(R.DEFAULT_ASC_ID_SORT_ORDER,
                pageNumber, pageSize);
        List<UserDto> userDtoList = userDtoAsm.toResources(users);
        return new ResponseEntity<List<UserDto>>(userDtoList, HttpStatus.OK);
    }

    /**
     * Create new {@link User}.
     * 
     * @param userDto - the {@link UserDto} for {@link User} to be created
     * @return {@link ResponseEntity} with created {@link User}
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDto> addNewUser(@RequestBody UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("User should not be null");
        }
        User user = userDto.overwriteEntity(new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(UserRole.ROLE_USER);
        if ((user.getImageLink() == null) && (user.getImageFile() == null)) {
            user.setImageLink(R.DUMMY_PICTURE);
        }
        user = userService.create(user);
        return new ResponseEntity<UserDto>(userDtoAsm.toResource(user), HttpStatus.OK);
    }

    /**
     * Get {@link UserDto} for username.
     * 
     * @param username - the name of needed user
     * @return - {@link ResponseEntity} with found {@link User}
     */
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(@PathVariable("username") String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username should not be null!");
        }
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<UserDto>(userDtoAsm.toResource(user), HttpStatus.OK);
    }

    /**
     * Update {@link User}.
     * 
     * @param userDto - the {@link UserDto} of the {@link User} to be updated
     * @return {@link UserDto} of updated {@link User}
     */
    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateUserById(
            @RequestBody UserDto userDto, @PathVariable("userId") int userId) {
        if (userDto == null) {
            throw new IllegalArgumentException("User should not be null");
        }
        User user = userService
                .update(userDto.overwriteEntity(userService.getById(userId)));
        return new ResponseEntity<UserDto>(userDtoAsm.toResource(user), HttpStatus.OK);
    }

    /**
     * Delete {@link User} from persistent context.
     * 
     * @param userId - the ID of the {@link User} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> deleteUserById(@PathVariable("userId") int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException(
                    "User id should be greater than 0," + " but have:" + userId);
        }
        userService.delete(userService.getById(userId));
        return new ResponseEntity<UserDto>(HttpStatus.OK);
    }

    /**
     * Get paginated comments for the given {@link User}. For retrieving all user comments please
     * set {@code pageNumber = 1} and {@code pageSize = Integer.MAX_VALUE}. If these parameters are
     * missed, the default values are: {@code pageNumber = 1} and {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @param userId - the ID of given {@link User}
     * @return {@link ResponseEntity} with {@link List} of {@link BlogComment}
     */
    @RequestMapping(path = "/{userId}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<BlogCommentDto>> getPaginatedUserComments(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize,
            @PathVariable("userId") int userId) {
        if ((pageNumber < 1) || (pageSize < 1) || (userId < 1)) {
            throw new IllegalArgumentException(
                    "pageNumber, pageSize and userId should be > 0" + " but have pageNumber="
                            + pageNumber + ", pageSize=" + pageSize + ", userId" + userId);
        }
        List<BlogComment> comments =
                userService.getUserCommentsByUserId(userId, pageNumber, pageSize);
        List<BlogCommentDto> userDtoList = new BlogCommentDtoAsm().toResources(comments);
        return new ResponseEntity<List<BlogCommentDto>>(userDtoList, HttpStatus.OK);
    }
}
