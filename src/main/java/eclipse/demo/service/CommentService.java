package eclipse.demo.service;


import eclipse.demo.domain.Alarm;
import eclipse.demo.domain.Board;
import eclipse.demo.domain.Comment;
import eclipse.demo.domain.ReComment;
import eclipse.demo.repository.AlarmRepository;
import eclipse.demo.repository.BoardRepository;
import eclipse.demo.repository.CommentRepository;
import eclipse.demo.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final ReCommentRepository reCommentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void save(Board board, String content){
        Board findBoard = boardRepository.findById(board.getId()).orElse(null);
        Comment comment = new Comment(findBoard, content);

        commentRepository.save(comment);

        Alarm alarm = new Alarm(findBoard, comment);
        alarmRepository.save(alarm);

    }

    @Transactional
    public void ssave(Board board, String content){
        Board findBoard = boardRepository.findById(board.getId()).orElse(null);

        Integer commentGroup = commentRepository.findCommentGroup().orElse(null);

        Comment comment = new Comment();
        comment.setBoard(findBoard);
        comment.setContent(content);
        if (commentGroup == null){
            comment.setCommentGroup(0);
        }
        else {
            comment.setCommentGroup(commentGroup+1);
        }
        Alarm alarm = new Alarm(findBoard, comment);
        commentRepository.save(comment);
        alarmRepository.save(alarm);

    }

    @Transactional
    public void resave(String content, Comment parent){
         Comment comment = new Comment();
         comment.setContent(content);
         comment.setCommentGroup(parent.getCommentGroup());
         comment.setCommentSequence(parent.getCommentSequence()+1);
         comment.setLevel(parent.getLevel()+1);
         comment.setParent(parent);

         commentRepository.save(comment);
    }


    public List<Comment> findJoinComment(Long boardId){
        List<Comment> findComments = commentRepository.findComment(boardId);

        return findComments;
    }

    public Comment findOne(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment;
    }

    public List<Comment> findAllByIdDesc(){
        List<Comment> findAll = commentRepository.findAllByOrderByIdDesc();
        return findAll;
    }

    public List<Comment> findAllComment(){
        List<Comment> comments = commentRepository.findAll();
        return comments;
    }
//<----------------- 대댓글 --------------------------->


    public List<ReComment> findReComment(Long boardId){

        List<ReComment> reComment = reCommentRepository.findReComment(boardId);
        return reComment;

    }

    public List<ReComment> ReFindAll(){
        List<ReComment> reComments = reCommentRepository.findAll();

        return reComments;
    }





}
