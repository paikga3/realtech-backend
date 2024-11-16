package realtech.api.front.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchGalleryPostsParams;
import realtech.api.front.model.GalleryPost;

@Service
public class GalleryPostService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    public PagedResponse<GalleryPost> fetchGalleryPost(FetchGalleryPostsParams rq) {
        StringBuffer where = new StringBuffer();
        where.append(" from ").append(rq.getEntity()).append(" t ");
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
        where.append(" order by t.postId desc ");
        
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT new realtech.api.front.model.GalleryPost(")
                .append("t.postId, t.title, t.thumbnailUrl) ")
                .append(where);
        
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<GalleryPost> listQuery = entityManager.createQuery(bodySelect.toString(), GalleryPost.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        PagedResponse<GalleryPost> pr = new PagedResponse<>();
        pr.setData(listQuery.getResultList());
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
    
}
