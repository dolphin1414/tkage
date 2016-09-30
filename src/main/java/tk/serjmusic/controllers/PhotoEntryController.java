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

import tk.serjmusic.controllers.dto.PhotoEntryDto;
import tk.serjmusic.controllers.dto.asm.PhotoEntryDtoAsm;
import tk.serjmusic.models.PhotoEntry;
import tk.serjmusic.services.PhotoEntryService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link PhotoEntry} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/photo")
public class PhotoEntryController {
    
    private static final PhotoEntryDtoAsm photoDtoAsm = new PhotoEntryDtoAsm();

    @Autowired
    private PhotoEntryService photoService;

    /**
     * Get {@link ResponseEntity} with the paginated list of {@link PhotoEntry} entities. 
     * For retrieving all photo entities please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link PhotoEntry}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PhotoEntryDto>> getPaginatedPhotos(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<PhotoEntry> photos = photoService.getPaginatedAndOrdered(R.DEFAULT_ASC_ID_SORT_ORDER,
                pageNumber, pageSize);
        List<PhotoEntryDto> photoDtoList = photoDtoAsm.toResources(photos);
        return new ResponseEntity<List<PhotoEntryDto>>(photoDtoList, HttpStatus.OK);
    }

    /**
     * Create new {@link PhotoEntry}.
     * 
     * @param photoDto - the {@link PhotoEntryDto} for {@link PhotoEntry} to be created
     * @return {@link ResponseEntity} with created {@link PhotoEntry}
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PhotoEntryDto> addNewPhotoEntry(@RequestBody PhotoEntryDto photoDto) {
        if (photoDto == null) {
            throw new IllegalArgumentException("Photo DTO should not be null");
        }
        PhotoEntry photo = photoDto.overwriteEntity(new PhotoEntry());
        photo = photoService.create(photo);
        return new ResponseEntity<PhotoEntryDto>(photoDtoAsm.toResource(photo), HttpStatus.OK);
    }

    /**
     * Get {@link PhotoEntryDto} for the given ID.
     * 
     * @param photoId - the ID of needed Photo Entry
     * @return - {@link ResponseEntity} with found {@link PhotoEntry}
     */
    @RequestMapping(path = "/{photoId}", method = RequestMethod.GET)
    public ResponseEntity<PhotoEntryDto> getPhotoById(@PathVariable("photoId") int photoId) {
        if (photoId < 0) {
            throw new IllegalArgumentException(
                    "Photo id should be greater than 0," + " but have:" + photoId);
        }
        PhotoEntry photo = photoService.getById(photoId);
        return new ResponseEntity<PhotoEntryDto>(photoDtoAsm.toResource(photo), HttpStatus.OK);
    }

    /**
     * Update {@link PhotoEntry}.
     * 
     * @param photoDto - the {@link PhotoEntryDto} of the {@link PhotoEntry} to be updated
     * @return {@link PhotoEntryDto} of updated {@link PhotoEntry}
     */
    @RequestMapping(path = "/{photoId}", method = RequestMethod.PUT)
    public ResponseEntity<PhotoEntryDto> updatePhotoById(
            @RequestBody PhotoEntryDto photoDto, @PathVariable("photoId") int photoId) {
        if (photoDto == null) {
            throw new IllegalArgumentException("Photo DTO should not be null");
        }
        PhotoEntry photo = photoService
                .update(photoDto.overwriteEntity(photoService.getById(photoId)));
        return new ResponseEntity<PhotoEntryDto>(photoDtoAsm.toResource(photo), HttpStatus.OK);
    }

    /**
     * Delete {@link PhotoEntry} from the persistent context.
     * 
     * @param photoId - the ID of the {@link PhotoEntry} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{photoId}", method = RequestMethod.DELETE)
    public ResponseEntity<PhotoEntryDto> deletePhotoById(@PathVariable("photoId") int photoId) {
        if (photoId < 0) {
            throw new IllegalArgumentException(
                    "Photo id should be greater than 0, but have:" + photoId);
        }
        photoService.delete(photoService.getById(photoId));
        return new ResponseEntity<PhotoEntryDto>(HttpStatus.OK);
    }
}
