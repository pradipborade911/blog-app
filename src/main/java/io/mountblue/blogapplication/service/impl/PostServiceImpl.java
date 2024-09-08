package io.mountblue.blogapplication.service.impl;

import io.mountblue.blogapplication.dto.CommentDTO;
import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.TagRepository;
import io.mountblue.blogapplication.service.PostService;
import io.mountblue.blogapplication.specification.PostSpecification;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PostSummaryDTO> findAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("failed to find post by id"));

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        for (Tag tag : post.getTags()) {
            postDTO.addTag(tag.getName());
        }
        return postDTO;
    }

    @Override
    public PostDTO savePost(PostDTO postRequestDTO) {
        Post post = modelMapper.map(postRequestDTO, Post.class);

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now());
        post.setPublished(true);

        for (String tagName : postRequestDTO.getTagsList()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        }

        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        for (Tag tag : savedPost.getTags()) {
            postDTO.addTag(tag.getName());
        }

        return postDTO;
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postRequestDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find Post with this id"));

        post.setTitle(postRequestDTO.getTitle());
        post.setExcerpt(postRequestDTO.getExcerpt());
        post.setContent(postRequestDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        post.getTags().clear();
        for (String tagName : postRequestDTO.getTagsList()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(tagName));
            post.addTag(tag);
        }

        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        for (Tag tag : savedPost.getTags()) {
            postDTO.addTag(tag.getName());
        }

        return postDTO;
    }

    @Override
    public List<PostSummaryDTO> deletePostById(Long id) {
        postRepository.deleteById(id);
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO addComment(CommentDTO commentDTO, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post dosen't exists"));

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);

        post.addComment(comment);
        Post savedPost = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);

        return postDTO;
    }

    @Override
    public PostDTO deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post dosen't exists"));
        Comment comment = post.getComments().stream()
                .filter(comment1 -> comment1.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The requested comment was not found for this post."
                ));

        post.removeComment(comment);
        post = postRepository.save(post);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return postDTO;
    }

    @Override
    public List<PostSummaryDTO> findAllPostsByAuthor(String author) {
        return postRepository.findAllByAuthor(author)
                .stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostSummaryDTO> findByAuthorsOrTags(List<String> authors, List<String> tags, int pageNumber, int pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equals("latest")?Sort.Direction.DESC:Sort.Direction.ASC, "createdAt"));

        Page<Post> paginatedPosts = postRepository.findByAuthorsOrTags(authors, tags, pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public List<String> findAllTags(){
        return tagRepository.findAll().stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }

    @Override
    public List<String> findAllAuthors() {
        return postRepository.findAllAuthors();
    }

    @Override
    public Page<PostSummaryDTO> findPaginatedPosts(int pageNumber, int pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equals("latest")?Sort.Direction.DESC:Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.findAll(pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

    @Override
    public Page<PostSummaryDTO> searchPaginatedPosts(String searchQuery, int pageNumber, int pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equals("latest")?Sort.Direction.DESC:Sort.Direction.ASC, "createdAt"));
        Page<Post> paginatedPosts = postRepository.searchPosts(searchQuery, pageable);
        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }


    public Page<PostSummaryDTO> findByAuthorsOrTagsSpec(List<String> authors, List<String> tags, int pageNumber, int pageSize, String order) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.equals("latest")?Sort.Direction.DESC:Sort.Direction.ASC, "createdAt"));

        LocalDate startdate = LocalDate.of(2030, 1, 1);
        LocalDate enddate = LocalDate.of(2030, 1, 1);

        LocalDateTime startOfDay = startdate.atStartOfDay();
        LocalDateTime endOfDay = enddate.atTime(23, 59, 59, 999999999);

        Page<Post> paginatedPosts = postRepository.findByAuthorInOrTagsNameInOrCreatedAtBetween(authors, tags, startOfDay, endOfDay, pageable);


//   Define specifications for each criterion
//        Specification<Post> authorSpec = PostSpecification.postHasAuthor(authors);
//        Specification<Post> tagSpec = PostSpecification.postHasTag(tags);
//        Specification<Post> dateSpec = PostSpecification.postHasDate(date);
//
//// Combine specifications with OR logic
//        Specification<Post> combinedSpec = Specification.where(authorSpec)
//                .or(tagSpec)
//                .or(dateSpec);
//        Page<Post> paginatedPosts = postRepository.findAll(combinedSpec, pageable);

        return paginatedPosts.map(post -> modelMapper.map(post, PostSummaryDTO.class));
    }

}
