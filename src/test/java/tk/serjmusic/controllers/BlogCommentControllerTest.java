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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import tk.serjmusic.controllers.dto.BlogCommentDto;
import tk.serjmusic.controllers.dto.asm.BlogCommentDtoAsm;
import tk.serjmusic.controllers.exceptions.ExceptionHandlerAdvice;
import tk.serjmusic.models.BlogComment;
import tk.serjmusic.models.BlogEntry;
import tk.serjmusic.models.User;
import tk.serjmusic.services.BlogCommentService;
import tk.serjmusic.utils.R;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Test module for {@link BlogCommentController}.
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
public class BlogCommentControllerTest {
    
    @Mock
    private BlogCommentService commentService;
    
    @InjectMocks
    private BlogCommentController blogController;
    
    private MockMvc mockMvc;
    private ArgumentCaptor<BlogComment> argumentCaptor;
    private ObjectMapper jsonMapper = new ObjectMapper();
    
    private BlogComment comment1;
    private BlogComment comment2;
    private User user;
    private BlogEntry blogEntry;
    

    /**
     * Set up method.
     * 
     * @throws java.lang.Exception sometimes happens
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogController)
                                 .setControllerAdvice(new ExceptionHandlerAdvice())
                                 .build();
        comment1 = new BlogComment("test1");
        comment2 = new BlogComment("test2");
        user = new User();
        comment1.setAuthor(user);
        comment2.setAuthor(user);
        comment1.setDateCreated(new Date());
        comment2.setDateCreated(new Date());
        user.getComments().addAll(Arrays.asList(comment1, comment2));
        blogEntry = new BlogEntry();
        blogEntry.setAuthor(user);
        blogEntry.setDateCreated(new Date());
        user.getBlogs().add(blogEntry);
        comment1.setBlogEntry(blogEntry);
        comment2.setBlogEntry(blogEntry);
        argumentCaptor = ArgumentCaptor.forClass(BlogComment.class);
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogCommentController
     * #getPaginatedComments(int, int)}.
     * 
     * @throws Exception sometimes
     */
    @Test
    public final void testGetPaginatedComments() throws Exception {
        List<BlogComment> comments = Arrays.asList(comment1, comment2);
        when(commentService.getPaginatedAndOrdered(
                eq(R.DEFAULT_ASC_ID_SORT_ORDER), anyInt(), anyInt()))
                .thenReturn(comments);
        String path = "/api/v1/resources/comments";
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())))
                .andExpect(jsonPath("$.[*].content", hasItem(comments.get(0).getContent())))
                .andExpect(jsonPath("$.[*].content", 
                        hasItem(comments.get(comments.size() - 1).getContent())));
        path = "/api/v1/resources/comments?pageNumber=-1&pageSize=1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
        path = "/api/v1/resources/comments?pageNumber=1&pageSize=-1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogCommentController#getCommentById(int)}.
     * @throws Exception sometimes
     */
    @Test
    public final void testGetCommentById() throws Exception {
        when(commentService.getById(anyInt())).thenReturn(comment1);
        String path = "/api/v1/resources/comments/1";
        mockMvc.perform(get(path))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", is(comment1.getContent())));
        path = "/api/v1/resources/comments/-1";
        mockMvc.perform(get(path))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogCommentController
     * #updateCommentById(tk.serjmusic.controllers.dto.BlogCommentDto, int)}.
     * 
     * @throws Exception sometimes
     */
    @Ignore
    @Test
    public final void testUpdateCommentById() throws Exception {
        BlogCommentDto commentDto = new BlogCommentDtoAsm().toResource(comment1);
        String commentJson = jsonMapper.writeValueAsString(commentDto);
        when(commentService.getById(anyInt())).thenReturn(comment1);
        when(commentService.update(anyObject())).thenReturn(comment1);
        String path = "/api/v1/resources/comments/1";
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON)
                                 .content(commentJson))
               .andExpect(status().isOk());
        verify(commentService).update(argumentCaptor.capture());
        assertEquals(comment1, argumentCaptor.getValue());
        path = "/api/v1/resources/comments/-1";
        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON)
                                 .content(commentJson))
               .andExpect(status().isBadRequest());
    }

    /**
     * Test method for {@link tk.serjmusic.controllers.BlogCommentController
     * #deleteCommentById(int)}.
     * 
     * @throws Exception sometimes
     */
    @Ignore
    @Test
    public final void testDeleteCommentById() throws Exception {
        doNothing().when(commentService).delete(anyObject());
        when(commentService.getById(anyInt())).thenReturn(comment1);
        String path = "/api/v1/resources/comments/1";
        mockMvc.perform(delete(path))
               .andExpect(status().isOk());
        verify(commentService).delete(argumentCaptor.capture());
        assertEquals(comment1, argumentCaptor.getValue());
        path = "/api/v1/resources/comments/-1";
        mockMvc.perform(delete(path))
                .andExpect(status().isBadRequest());
    }

}
