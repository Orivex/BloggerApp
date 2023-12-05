package com.blacklight.blogger.controllers;
import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.services.AuthorService;
import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.services.BlogService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
	
	private final BlogService blogService;
	
	public BlogController(BlogService blogService) {
		this.blogService = blogService;
	}
	
	@GetMapping("/searchByTitle/{title}")
	public Blog searchBlogByTitle(@PathVariable String title) {
		Blog blog = blogService.getBlogByTitle(title);
		if(blog == null) {
			return new Blog();
		}

		return blog;
	}

	@GetMapping("/searchByAuthorName/{authorName}")
	public List<Blog> searchBlogsByAuthorName(@PathVariable String authorName) {
		List<Blog> blogs = blogService.getBlogsByAuthorName(authorName);
		if(blogs == null) {
			return new ArrayList<>();
		}

		return blogs;
	}

	@GetMapping("/getBlogById/{id}")
	public Blog getBlogById(@PathVariable Long id) {
		return blogService.getBlogById(id);
	}

	@GetMapping("/getBlogByTitle/{title}")
	public Blog getBlogById(@PathVariable String title) {
		return blogService.getBlogByTitle(title);
	}

	@GetMapping("/getBlogTitleById/{id}")
	public String getBlogTitleById(@PathVariable Long id) {
		return blogService.getBlogTitleById(id);
	}

	@GetMapping("/getAllBlogs")
	public List<Blog> getAllBlogs() {
		return blogService.getAllBlogs();
	}

	@GetMapping("/size")
	public int allBlogsSize() {
		return blogService.allBlogsSize();
	}
}
