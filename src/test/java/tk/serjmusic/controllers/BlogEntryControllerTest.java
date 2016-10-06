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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tk.serjmusic.controllers.dto.BlogEntryDto;
import tk.serjmusic.controllers.dto.asm.BlogEntryDtoAsm;
import tk.serjmusic.controllers.exceptions.ExceptionHandlerAdvice;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.services.BlogEntryService;

import java.util.Arrays;
import java.util.List;

/**
 * Test module for {@link BlogEntryController}.
 *
 * @author Roman Kondakov
 */

@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(locations = {"classpath:spring-test-dao.xml"}),
        @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/tkpage-servlet.xml", 
                "classpath:tkpage-security-test.xml"})
         })
@RunWith(SpringJUnit4ClassRunner.class)
public class BlogEntryControllerTest {
    
    @Mock
    private BlogEntryService blogService;
    
    @InjectMocks
    private BlogEntryController blogController;
    
    private MockMvc mockMvc;
    private ArgumentCaptor<BlogEntry> argumentCaptor;
    private ObjectMapper jsonMapper = new ObjectMapper();
    
    private BlogComment comment1;
    private BlogComment comment2;
    private User user;
    private BlogEntry blogEntry1;
    private BlogEntry blogEntry2;
    

    /**
     * Set up method.
     * 
     * @throws java.lang.Exception sometimes
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogController)
                                 .setControllerAdvice(new ExceptionHandlerAdvice())
                                 .build();
        argumentCaptor = ArgumentCaptor.forClass(BlogEntry.class);
        
        comment1 = new BlogComment("test_comment_1");
        comment2 = new BlogComment("test_comment_2");
        user = new User("testUser");
        blogEntry1 = new BlogEntry("test_blogEntry_1");
        blogEntry2 = new BlogEntry("test_blogEntry_2");
        
        comment1.setAuthor(user);
        comment2.setAuthor(user);
        comment1.setBlogEntry(blogEntry1);
        comment2.setBlogEntry(blogEntry1);
        
        user.getComments().add(comment1);
        user.getComments().add(comment2);
        user.getBlogs().add(blogEntry1);
        user.getBlogs().add(blogEntry2);
        
        blogEntry1.getComments().add(comment1);
        blogEntry1.getComments().add(comment2);
        blogEntry1.setAuthor(user);
        blogEntry2.setAuthor(user);
        
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController
     * #getPaginatedBlogs(int, int)}.
     * @throws Exception  sometimes
     */
    @Test
    public final void testGetPaginatedBlogs() throws Exception {
        List<BlogEntry> blogs = Arrays.asList(blogEntry1, blogEntry2);
        when(blogService.getPaginatedAndOrdered(anyBoolean(), anyInt(), anyInt()))
                .thenReturn(blogs);
        String path = "/api/v1/resources/blogs";
        mockMvc.perform(get(path)).andExpect(status().isOk())
                                  .andExpect(jsonPath("$", hasSize(blogs.size())))
                                  .andExpect(jsonPath("$.[*].content", 
                                          hasItem(blogEntry1.getContent())));
        path = "/api/v1/resources/blogs?pageNumber=-1&pageSize=1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
        path = "/api/v1/resources/blogs?pageNumber=1&pageSize=-1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController
     * #addNewBlogEntry(tk.serjmusic.controllers.dto.BlogEntryDto)}.
     * 
     * @throws Exception sometimes
     */
    @Test
    public final void testAddNewBlogEntry() throws Exception {
        when(blogService.create(any())).thenReturn(blogEntry1);
        String path = "/api/v1/resources/blogs";
        BlogEntryDto blogDto = new BlogEntryDtoAsm().toResource(blogEntry1);
        String blogJson = jsonMapper.writeValueAsString(blogDto);
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(blogJson)).andExpect(status().isOk());
        verify(blogService).create(argumentCaptor.capture());
        assertEquals(blogEntry1.getTitle(), argumentCaptor.getValue().getTitle());
        path = "/api/v1/resources/blogs/-1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController#getBlogById(int)}.
     */
    @Ignore
    @Test
    public final void testGetBlogById() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController
     * #updateBlogById(tk.serjmusic.controllers.dto.BlogEntryDto, int)}.
     */
    @Ignore
    @Test
    public final void testUpdateBlogById() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController#deleteUserById(int)}.
     */
    @Ignore
    @Test
    public final void testDeleteUserById() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController
     * #getPaginatedCommentsForBlog(int, int, int)}.
     */
    @Ignore
    @Test
    public final void testGetPaginatedCommentsForBlog() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogEntryController
     * #addNewCommentForBlogId
     * (int, tk.serjmusic.controllers.dto.BlogCommentDto)}.
     */
    @Ignore
    @Test
    public final void testAddNewCommentForBlogId() {
        fail("Not yet implemented"); // TODO
    }

}
