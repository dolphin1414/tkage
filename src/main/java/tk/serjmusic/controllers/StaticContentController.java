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

package tk.serjmusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tk.serjmusic.controllers.dto.StaticContentDto;
import tk.serjmusic.controllers.dto.asm.StaticContentDtoAsm;
import tk.serjmusic.models.StaticContent;
import tk.serjmusic.services.StaticContentService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link StaticContent} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/static")
public class StaticContentController {
    
    private static final StaticContentDtoAsm staticDtoAsm = new StaticContentDtoAsm();

    @Autowired
    private StaticContentService staticService;

    /**
     * Get {@link ResponseEntity} with the paginated list of {@link StaticContent} entities. 
     * For retrieving all staticContent entities please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link StaticContent}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<StaticContentDto>> getPaginatedStatics(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<StaticContent> statics = staticService.getPaginatedAndOrdered(
                R.DEFAULT_ASC_ID_SORT_ORDER, pageNumber, pageSize);
        List<StaticContentDto> staticDtoList = staticDtoAsm.toResources(statics);
        return new ResponseEntity<List<StaticContentDto>>(staticDtoList, HttpStatus.OK);
    }

    /**
     * Create new {@link StaticContent}.
     * 
     * @param staticDto - the {@link StaticContentDto} for {@link StaticContent} to be created
     * @return {@link ResponseEntity} with created {@link StaticContent}
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StaticContentDto> addNewStaticContent(
            @RequestBody StaticContentDto staticDto) {
        if (staticDto == null) {
            throw new IllegalArgumentException("Static DTO should not be null");
        }
        StaticContent staticContent = staticDto.overwriteEntity(new StaticContent());
        staticContent = staticService.create(staticContent);
        return new ResponseEntity<StaticContentDto>(staticDtoAsm.toResource(staticContent),
                HttpStatus.OK);
    }

    /**
     * Get {@link StaticContentDto} for the given ID.
     * 
     * @param staticId - the ID of needed Static Entry
     * @return - {@link ResponseEntity} with found {@link StaticContent}
     */
    @RequestMapping(path = "/{description}", method = RequestMethod.GET)
    public ResponseEntity<StaticContentDto> getStaticByDescription(
            @PathVariable("description") String description) {
        if (description == null) {
            throw new IllegalArgumentException(
                    "Description should be not null");
        }
        StaticContent staticContent = staticService.getStaticContentByDescription(description);
        return new ResponseEntity<StaticContentDto>(staticDtoAsm.toResource(staticContent),
                HttpStatus.OK);
    }

    /**
     * Update {@link StaticContent}.
     * 
     * @param staticDto - the {@link StaticContentDto} of the {@link StaticContent} to be updated
     * @return {@link StaticContentDto} of updated {@link StaticContent}
     */
    @RequestMapping(path = "/{staticId}", method = RequestMethod.PUT)
    public ResponseEntity<StaticContentDto> updateStaticById(
            @RequestBody StaticContentDto staticDto, @PathVariable("staticId") int staticId) {
        if (staticDto == null) {
            throw new IllegalArgumentException("Static DTO should not be null");
        }
        StaticContent staticContent = staticService.update(staticDto.overwriteEntity(
                staticService.getById(staticId)));
        return new ResponseEntity<StaticContentDto>(
                staticDtoAsm.toResource(staticContent), HttpStatus.OK);
    }

    /**
     * Delete {@link StaticContent} from the persistent context.
     * 
     * @param staticId - the ID of the {@link StaticContent} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{staticId}", method = RequestMethod.DELETE)
    public ResponseEntity<StaticContentDto> deleteStaticById(
            @PathVariable("staticId") int staticId) {
        if (staticId < 0) {
            throw new IllegalArgumentException(
                    "Static id should be greater than 0, but have:" + staticId);
        }
        staticService.delete(staticService.getById(staticId));
        return new ResponseEntity<StaticContentDto>(HttpStatus.OK);
    }
}
