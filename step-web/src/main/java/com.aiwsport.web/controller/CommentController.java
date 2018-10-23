package com.aiwsport.web.controller;


import java.util.ArrayList;
import java.util.List;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Comment;
import com.aiwsport.core.entity.QueryCommentShow;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class CommentController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StepService stepService;

    @RequestMapping("/get_comments.json")
    public ResultMsg getComments(Integer activeStepId) {
        logger.info("getComments");
        List<Comment> comments = stepService.getComment(activeStepId);
        List<QueryCommentShow> queryCommentShows = new ArrayList<QueryCommentShow>();

        for (Comment comment : comments) {
            QueryCommentShow queryCommentShow =  new QueryCommentShow();
            queryCommentShow.setCommentid(comment.getId());
            queryCommentShow.setActivestepid(comment.getActivestepid());
            queryCommentShow.setUserid(comment.getUserid());
            queryCommentShow.setContent(comment.getContent());
            queryCommentShow.setCreatetime(comment.getCreatetime());

            User user = stepService.getUser(comment.getUserid()+"");
            if (user != null) {
                queryCommentShow.setCity(user.getCity());
                queryCommentShow.setCountry(user.getCountry());
                queryCommentShow.setProvince(user.getProvince());
                queryCommentShow.setAvatarurl(user.getAvatarurl());
                queryCommentShow.setCoinnum(user.getCoinnum());
                queryCommentShow.setNickname(user.getNickname());
                queryCommentShow.setGender(user.getGender());
            }
            queryCommentShows.add(queryCommentShow);
        }

        return new ResultMsg("getCommentsOk", queryCommentShows);
    }

    @RequestMapping("/create_comment.json")
    public ResultMsg createComment(Integer userId, Integer activeStepId, String content) {
        logger.info("createComment");
        int isSuccess = 0;
        try{
            isSuccess = stepService.createComment(userId, activeStepId, content);
        }catch(Exception e){
            logger.error("createComment is error activeStepId is " + activeStepId + "content is " + content, e);
            return new ResultMsg(false, 403, "评论失败");
        }

        if (isSuccess == 0) {
            return new ResultMsg(false, 403, "评论失败");
        }

        return new ResultMsg("createCommentOk", "评论成功");
    }

    @RequestMapping("/step/del_comment.json")
    public ResultMsg delComment(Integer userId, Integer activeStepId) {
        logger.info("delComment");
        int isSuccess = 0;
        try{
            isSuccess = stepService.delComment(userId, activeStepId);
        }catch(Exception e){
            logger.error("delComment is error activeStepId is " + activeStepId, e);
            return new ResultMsg(false, 403, "评论删除失败");
        }

        if (isSuccess == 0) {
            return new ResultMsg(false, 403, "评论删除失败");
        }

        return new ResultMsg("createCommentOk", "评论删除成功");
    }
}