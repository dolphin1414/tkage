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

import tk.serjmusic.controllers.dto.VideoEntryDto;
import tk.serjmusic.controllers.dto.asm.VideoEntryDtoAsm;
import tk.serjmusic.models.VideoEntry;
import tk.serjmusic.services.VideoEntryService;
import tk.serjmusic.utils.R;

import java.util.List;

/**
 * The MVC controller for {@link VideoEntry} resources requests.
 *
 * @author Roman Kondakov
 */

@Controller
@RequestMapping("api/v1/resources/video")
public class VideoEntryController {
// TODO
//    api/v1/resources/video – all video
//    api/v1/resources/video/{videoId} – concrete video
//    api/v1/resources/video?pageNumber={int}&pageSize={int} – paginated video
    
    private static final VideoEntryDtoAsm videoDtoAsm = new VideoEntryDtoAsm();

    @Autowired
    private VideoEntryService videoService;

    /**
     * Get {@link ResponseEntity} with the paginated list of {@link VideoEntry} entities. 
     * For retrieving all video entities please set {@code pageNumber = 1} and 
     * {@code pageSize = Integer.MAX_VALUE}.
     * If these parameters are missed, the default values are: {@code pageNumber = 1} and
     * {@code pageSize = 10}.
     * 
     * @param pageNumber - the number of retrieving page
     * @param pageSize - the size of retrieving page
     * @return {@link ResponseEntity} with {@link List} of {@link VideoEntry}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<VideoEntryDto>> getPaginatedVideos(
            @RequestParam(name = "pageNumber", defaultValue = R.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = R.DEFAULT_PAGE_SIZE) int pageSize) {
        if ((pageNumber < 1) || (pageSize < 1)) {
            throw new IllegalArgumentException("pageNumber and pageSize should be > 0"
                    + " but have pageNumber=" + pageNumber + ", pageSize=" + pageSize);
        }
        List<VideoEntry> videos = videoService.getPaginatedAndOrdered(R.DEFAULT_ASC_ID_SORT_ORDER,
                pageNumber, pageSize);
        List<VideoEntryDto> videoDtoList = videoDtoAsm.toResources(videos);
        return new ResponseEntity<List<VideoEntryDto>>(videoDtoList, HttpStatus.OK);
    }

    /**
     * Create new {@link VideoEntry}.
     * 
     * @param videoDto - the {@link VideoEntryDto} for {@link VideoEntry} to be created
     * @return {@link ResponseEntity} with created {@link VideoEntry}
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<VideoEntryDto> addNewVideoEntry(@RequestBody VideoEntryDto videoDto) {
        if (videoDto == null) {
            throw new IllegalArgumentException("Video DTO should not be null");
        }
        VideoEntry video = videoDto.overwriteEntity(new VideoEntry());
        video = videoService.create(video);
        return new ResponseEntity<VideoEntryDto>(videoDtoAsm.toResource(video), HttpStatus.OK);
    }

    /**
     * Get {@link VideoEntryDto} for the given ID.
     * 
     * @param videoId - the ID of needed Video Entry
     * @return - {@link ResponseEntity} with found {@link VideoEntry}
     */
    @RequestMapping(path = "/{videoId}", method = RequestMethod.GET)
    public ResponseEntity<VideoEntryDto> getVideoById(@PathVariable("videoId") int videoId) {
        if (videoId < 0) {
            throw new IllegalArgumentException(
                    "Video id should be greater than 0," + " but have:" + videoId);
        }
        VideoEntry video = videoService.getById(videoId);
        return new ResponseEntity<VideoEntryDto>(videoDtoAsm.toResource(video), HttpStatus.OK);
    }

    /**
     * Update {@link VideoEntry}.
     * 
     * @param videoDto - the {@link VideoEntryDto} of the {@link VideoEntry} to be updated
     * @return {@link VideoEntryDto} of updated {@link VideoEntry}
     */
    @RequestMapping(path = "/{videoId}", method = RequestMethod.PUT)
    public ResponseEntity<VideoEntryDto> updateVideoById(@RequestBody VideoEntryDto videoDto) {
        if (videoDto == null) {
            throw new IllegalArgumentException("Video DTO should not be null");
        }
        VideoEntry video = videoService
                .update(videoDto.overwriteEntity(videoService.getById(videoDto.getVideoEntryId())));
        return new ResponseEntity<VideoEntryDto>(videoDtoAsm.toResource(video), HttpStatus.OK);
    }

    /**
     * Delete {@link VideoEntry} from the persistent context.
     * 
     * @param videoId - the ID of the {@link VideoEntry} to be deleted
     * @return {@link HttpStatus.OK} in case of successful deletion.
     */
    @RequestMapping(path = "/{videoId}", method = RequestMethod.DELETE)
    public ResponseEntity<VideoEntryDto> deleteVideoById(@PathVariable("videoId") int videoId) {
        if (videoId < 0) {
            throw new IllegalArgumentException(
                    "Video id should be greater than 0, but have:" + videoId);
        }
        videoService.delete(videoService.getById(videoId));
        return new ResponseEntity<VideoEntryDto>(HttpStatus.OK);
    }
}
