package com.cdac.blog.services.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cdac.blog.entities.Category;
import com.cdac.blog.entities.Post;
import com.cdac.blog.entities.User;
import com.cdac.blog.exceptions.ResourceNotFoundException;
import com.cdac.blog.payloads.PostDto;
import com.cdac.blog.payloads.PostResponse;
import com.cdac.blog.repositories.CategoryRepo;
import com.cdac.blog.repositories.PostRepo;
import com.cdac.blog.repositories.UserRepo;
import com.cdac.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

		this.postRepo.delete(post);

	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);

		List<Post> allPosts = pagePost.getContent();

		List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());

		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		System.out.println("*******************************************************************");
		System.out.println(postDtos);

		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		Collections.reverse(posts);		
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}
	
	
	//getallByCategory
	
		@Override
		public PostResponse getAllCatPost(Integer categoryId, Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
			
			System.out.println(categoryId);
		
			
			Category cat=this.categoryRepo.findById(categoryId)
					.orElseThrow(()->new ResourceNotFoundException("Category","Category id",categoryId));
			
			System.out.println(cat);
			
			Sort sort=null;
			if(sortDir.equalsIgnoreCase("asc"))
			{
				sort=Sort.by(sortBy).ascending();
			}else
			{
				sort=Sort.by(sortBy).descending();
			}
			Pageable p=PageRequest.of(pageNumber, pageSize,sort);
			List<Post> allposts2 = this.postRepo.findByCategory(cat);									//to get all the posts.
			Collections.reverse(allposts2);																//used collection for sort.
			final Page<Post> pagePost = new PageImpl<Post>(allposts2,p,allposts2.size());
			List<PostDto> postDtos2=allposts2.stream().map((post2)->this.modelMapper.map(post2,PostDto.class)).collect(Collectors.toList());
			
			System.out.println("*******************************************************************");
			System.out.println(postDtos2);
			
			PostResponse postResponse=new PostResponse();
			
			postResponse.setContent(postDtos2);
			postResponse.setPageNumber(pagePost.getNumber());
			postResponse.setPageSize(pagePost.getSize());
			postResponse.setTotalElements(pagePost.getTotalElements());
			postResponse.setTotalPages(pagePost.getTotalPages());
			postResponse.setLastPage(pagePost.isLast());
			
			return postResponse;
		}
		
		
		

}


