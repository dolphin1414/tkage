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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Test case for hashCode()/equals contract for blog  entry. 
 *
 * @author Roman Kondakov
 */
public class BlogEntryTest {
    
    /**
     * <p>Testing hC/e contract using EqualsVerifier library.</p>
     *
     * <p>Test method for {@link tk.serjmusic.models.BlogEntry#equals(java.lang.Object)} and
     * {@link tk.serjmusic.models.BlogEntry#hashCode(java.lang.Object)}</p>
     */
    @Test
    public final void testHashCodeEqualsContract() {
        EqualsVerifier.forClass(BlogEntry.class)
                .withPrefabValues(User.class, new User("a"), new User("b"))
                .withPrefabValues(BlogComment.class, new BlogComment("a"), new BlogComment("b"))
                .withPrefabValues(BlogEntry.class, new BlogEntry("a"), new BlogEntry("b"))
                .withPrefabValues(HashSet.class, 
                        new HashSet<BlogComment>(Arrays.asList(new BlogComment("a"))),
                        new HashSet<BlogComment>(Arrays.asList(new BlogComment("b"))))
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .verify();
    }
}
