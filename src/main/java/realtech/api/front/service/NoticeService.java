package realtech.api.front.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchNoticesParams;
import realtech.api.front.model.Notice;
import realtech.db.entity.Attachment;
import realtech.db.entity.Comment;
import realtech.db.repository.AttachmentRepository;
import realtech.db.repository.CommentRepository;
import realtech.util.AppUtil;

@Service
public class NoticeService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    /**
     * 공지사항 리스트 가져오기
     * 
     * @param rq
     * @return
     */
    public PagedResponse<Notice> fetchNotices(FetchNoticesParams rq) {
        
        StringBuffer where = new StringBuffer();
        where.append(" from Notice t ");
        where.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            if ("title".equals(rq.getSearchType())) {
                where.append(" and t.title like concat('%',:searchKeyword,'%') ");
            } else if ("content".equals(rq.getSearchType())) {
                where.append(" and t.content like concat('%',:searchKeyword,'%') ");
            } else if ("tc".equals(rq.getSearchType())) {
                where.append(" and (t.title like concat('%',:searchKeyword,'%') or t.content like concat('%',:searchKeyword,'%')) ");
            }
        }
        
        
        where.append(" order by t.noticeId desc ");
        
        
        
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT new realtech.api.front.model.Notice(")
                .append("t.noticeId, t.title, t.authorName, t.createdAt) ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<Notice> listQuery = entityManager.createQuery(bodySelect.toString(), Notice.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        
        List<Notice> posts = listQuery.getResultList();
        for (Notice post : posts) {
            List<Comment> comments = commentRepository.findByRefTableAndRefId("notice", post.getId())
                    .stream()
                    .filter(a -> {
                        return a.getIsDeleted() == 0;
                    })
                    .collect(Collectors.toList());
            

            List<Attachment> attachments = attachmentRepository.findByRefTableAndRefId("notice", post.getId());
            
            post.setCommentCount(comments.size());
            post.setHasAttachment(!attachments.isEmpty());

            
            // 등록한지 10분 이내인 경우 신규글 마킹
            post.setNew(AppUtil.isNewPost(post.getCreatedAt()));
        }
        
        PagedResponse<Notice> pr = new PagedResponse<>();
        pr.setData(posts);
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
}
