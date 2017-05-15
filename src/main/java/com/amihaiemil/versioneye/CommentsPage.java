/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.amihaiemil.versioneye;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A page of Comments.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
final class CommentsPage implements Page<Comment> {

    /**
     * Comments.
     */
    private Comments comments;
    
    /**
     * Number of this page.
     */
    private int number;
    
    /**
     * Ctor.
     * @param comments Comments from this page.
     */
    CommentsPage(final Comments comments) {
        this(comments, 1);
    }
    
    /**
     * Ctor.
     * @param comments Comments from this page.
     * @param number Number of this page.
     */
    CommentsPage(final Comments comments, final int number) {
        this.comments = comments;
        this.number = number;
    }
    
    @Override
    public List<Comment> fetch() throws IOException {
        return this.comments.fetch(this.number);
    }

    @Override
    public Paging paging() throws IOException {
        return this.comments.paging(this.number);
    }

    @Override
    public Iterator<Page<Comment>> iterator() {
        return new CommentsPageIt(this.comments);
    }

    /**
     * Iterator over the comments pages.
     */
    private final class CommentsPageIt implements Iterator<Page<Comment>> {
        
        /**
         * Comments.
         */
        private Comments comments;
        
        /**
         * Number of this page.
         */
        private int number = 1;  
        
        /**
         * Ctor.
         * @param comments Comments from this page.
         */
        CommentsPageIt(final Comments comments) {
            this.comments = comments;
        }
        
        @Override
        public boolean hasNext() {
            try {
                final Paging paging = this.comments.paging(this.number);
                return paging.currentPage() <= paging.totalPages();
            } catch (final IOException ex) {
                throw new IllegalStateException(
                    "IOException occured when checking "
                    + "comments page number " + this.number, ex
                );
            }
        }

        @Override
        public Page<Comment> next() {
            final Page<Comment> next = new CommentsPage(
                this.comments, this.number
            );
            this.number++;
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "Cannot remove a page of comments!"
            );        
        }

    }

}
