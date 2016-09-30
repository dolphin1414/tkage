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

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import tk.serjmusic.controllers.BlogCommentController;
import tk.serjmusic.controllers.dto.BlogEntryDto;
import tk.serjmusic.models.BlogEntry;

/**
 * DTO assembler for {@link BlogEntry} entity.
 *
 * @author Roman Kondakov
 */
public class BlogEntryDtoAsm extends ResourceAssemblerSupport<BlogEntry, BlogEntryDto> {
    
    private static final UserDtoAsm userDtoAsm = new UserDtoAsm();

    public BlogEntryDtoAsm() {
        super(BlogCommentController.class, BlogEntryDto.class);
    }

    /* (non-Javadoc)
     * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
     */
    @Override
    public BlogEntryDto toResource(BlogEntry blogEntry) {
        BlogEntryDto blogEntryDto = new BlogEntryDto();
        blogEntryDto.setAuthor(userDtoAsm.toResource(blogEntry.getAuthor()));
        blogEntryDto.setBlogId(blogEntry.getId());
        blogEntryDto.setContent(blogEntry.getContent());
        blogEntryDto.setDateCreated(blogEntry.getDateCreated());
        blogEntryDto.setImageLink(blogEntry.getImageLink());
        blogEntryDto.setTitle(blogEntry.getTitle());
        //TODO add links
        return blogEntryDto;
    }

}
