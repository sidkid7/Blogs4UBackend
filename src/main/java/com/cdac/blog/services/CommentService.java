package com.cdac.blog.services;

import com.cdac.blog.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Integer postId);

	void deleteComment(Integer commentId);

}
