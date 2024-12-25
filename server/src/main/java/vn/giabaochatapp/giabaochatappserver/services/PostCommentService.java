package vn.giabaochatapp.giabaochatappserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.config.NotificationHandler;
import vn.giabaochatapp.giabaochatappserver.config.exception.AccessException;
import vn.giabaochatapp.giabaochatappserver.config.exception.NotFoundException;
import vn.giabaochatapp.giabaochatappserver.data.domains.Post;
import vn.giabaochatapp.giabaochatappserver.data.domains.PostComment;
import vn.giabaochatapp.giabaochatappserver.data.domains.User;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.PostCommentDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.PostIdDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.CommentPostRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.DeleteCommentRequest;
import vn.giabaochatapp.giabaochatappserver.data.repository.PostCommentRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.PostRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostCommentService {
    @Autowired
    public final PostCommentRepository postCommentRepository;

    @Autowired
    public final PostRepository postRepository;

    @Autowired
    public final UserRepository userRepository;

    @Autowired
    private NotificationHandler notificationHandler;

    public PostCommentService(PostCommentRepository postCommentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void commentPost(CommentPostRequest request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        PostComment postComment = new PostComment(postId, userId, request.getContent());
        postCommentRepository.save(postComment);

        Long authorId = existingPost.get().getAuthorId();
        if (Objects.equals(authorId, userId)) return;
        String notificationMessage = principal.getUsername() + " commented in your post!";
        try {
            System.out.println("Notification message: " + notificationMessage);
            notificationHandler.sendNotification(authorId, notificationMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCommentPost(DeleteCommentRequest request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Long commentId = request.getCommentId();
        Optional<PostComment> commentOpt = postCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }
        PostComment comment = commentOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        if (!comment.getUserId().equals(userId)) {
            throw new AccessException("You can only delete your comment");
        }
        postCommentRepository.delete(comment);
        // check post exist
        // check comment id exist
        // check comment belong to user
        // delete comment
    }

    public List<PostCommentDTO> getAllComments(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        return postCommentRepository.findAll().stream()
                .filter(comment -> Objects.equals(postId, comment.getPostId()))
                .map(comment -> {
                    String username = userRepository.findById(comment.getUserId())
                            .map(User::getUsername)
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    return new PostCommentDTO(comment, username);
                })
                .collect(Collectors.toList());
    }

    public Integer getCommentCount(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        return postCommentRepository.getCommentCountByPostId(postId);
    }
}
