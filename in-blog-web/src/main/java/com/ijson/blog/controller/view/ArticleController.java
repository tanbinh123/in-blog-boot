package com.ijson.blog.controller.view;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.exception.BlogNotFoundException;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/10 11:19 PM
 */
@Slf4j
@Controller()
@RequestMapping("/article")
public class ArticleController extends BaseController {
    //http://localhost:8876/article/cuiyongxu/details/1575744843


    /**
     * @param ename
     * @param shamId
     * @return
     */
    @RequestMapping("/{ename}/details/{shamId}")
    public ModelAndView details(HttpServletRequest request, @PathVariable("ename") String ename, @PathVariable("shamId") String shamId) {
        ModelAndView view = new ModelAndView(getViewTheme() + "/index-article.html");
        try {
            PostEntity entity = postService.findByShamId(ename, shamId);
            view.addObject("data", PostInfo.create(entity));
            view.addObject("path", "/");
            view.addObject("reply", getReplyList(ename, shamId));
            addViewModelAndView(request, view);
            return view;
        } catch (BlogNotFoundException e) {
            view.setViewName("error/404.html");
            return view;
        }
    }

    public Comment.ListResult getReplyList(String ename, String shamId) {
        Page page = new Page(500, 1, "", false);
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setEname(ename);
        commentQuery.setShamId(shamId);
        PageResult<CommentEntity> pageResult = commentService.find(commentQuery, page);
        List<Comment.CommentResult> result = Comment.CommentResult.transform(pageResult);
        return new Comment.ListResult(result, new Pageable(((Long) pageResult.getTotal()).intValue(), page.getPageNumber()));
    }
}
