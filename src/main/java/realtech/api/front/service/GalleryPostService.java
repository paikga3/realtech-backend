package realtech.api.front.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import realtech.api.common.exception.PostNotFoundException;
import realtech.api.common.model.PagedResponse;
import realtech.api.common.service.S3Service;
import realtech.api.front.model.CreateGalleryPostParams;
import realtech.api.front.model.FetchGalleryPostsParams;
import realtech.api.front.model.FileItem;
import realtech.api.front.model.GalleryPost;
import realtech.api.front.model.GalleryPostDetailData;
import realtech.db.entity.Attachment;
import realtech.db.entity.CeilingTvPost;
import realtech.db.entity.WallTvPost;
import realtech.db.repository.AttachmentRepository;
import realtech.db.repository.CeilingTvPostRepository;
import realtech.db.repository.WallTvPostRepository;
import realtech.util.AppUtils;

@Service
public class GalleryPostService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    @Autowired
    private WallTvPostRepository wallTvPostRepository;
    
    @Autowired
    private CeilingTvPostRepository ceilingTvPostRepository;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private S3Service s3Service;
    
    
    
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
    
    
    
    public GalleryPostDetailData getPostDetailByIdAndEntity(int id, String entity) {

        if ("WallTvPost".equals(entity)) {
            return fetchWallTvPostById(id);
        } else if ("CeilingTvPost".equals(entity)) {
            return fetchCeilingTvPostById(id);
        } else {
            throw new IllegalArgumentException("지원하지 않는 엔터티입니다: " + entity);
        }
    }
    
    
    public GalleryPostDetailData fetchWallTvPostById(int id) {
        // 1. 게시글 조회
        Optional<WallTvPost> postOpt = wallTvPostRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        WallTvPost post = postOpt.get();
        
        // 2. 조회수 증가
        post.setViews(post.getViews() + 1);
        wallTvPostRepository.save(post); // 조회수 업데이트
        
        // 3. 첨부파일 조회
        List<FileItem> attachments = attachmentRepository.findByRefTableAndRefId("wall_tv_post", id)
                .stream()
                .map(f -> new FileItem(f.getDisplayFilename(), f.getFileSizeKb().doubleValue(), "/api/attachments/download/" + f.getAttachmentId()))
                .collect(Collectors.toList());
        
        
        return new GalleryPostDetailData(
                id,
                post.getTitle(),
                post.getContent(),
                AppUtils.convertDateFormat(post.getCreatedAt()),
                post.getViews(),
                attachments
            );
    }
    
    public GalleryPostDetailData fetchCeilingTvPostById(int id) {
        Optional<CeilingTvPost> postOpt = ceilingTvPostRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        CeilingTvPost post = postOpt.get();
        
        // 2. 조회수 증가
        post.setViews(post.getViews() + 1);
        ceilingTvPostRepository.save(post); // 조회수 업데이트
        
        // 3. 첨부파일 조회
        List<FileItem> attachments = attachmentRepository.findByRefTableAndRefId("ceiling_tv_post", id)
                .stream()
                .map(f -> new FileItem(f.getDisplayFilename(), f.getFileSizeKb().doubleValue(), "/api/attachments/download/" + f.getAttachmentId()))
                .collect(Collectors.toList());
        
        return new GalleryPostDetailData(
                id,
                post.getTitle(),
                post.getContent(),
                AppUtils.convertDateFormat(post.getCreatedAt()),
                post.getViews(),
                attachments
            );
    }
    
    @Transactional
    public void createGalleryPost(CreateGalleryPostParams params, HttpServletRequest request) {

        if ("WallTvPost".equals(params.getEntity())) {
            // 1. 썸네일 파일 s3에 업로드
            String thumbnailUrl = s3Service.uploadFile(params.getThumbnail(), AppUtils.generateEditorPath("wall_tv_post"));
            
            
            // 2. 첨부 파일 s3에 업로드
            List<Attachment> attachments = new ArrayList<>();
            if (params.getAttachments() != null) {
                for (MultipartFile file : params.getAttachments()) {
                    String filePath = s3Service.uploadFile(file, AppUtils.generateAttachmentPath("wall_tv_post"));
                    
                    Attachment attachment = new Attachment();
                    attachment.setDisplayFilename(file.getOriginalFilename());
                    attachment.setFileSizeKb(AppUtils.getFileSizeInKB(file));
                    attachment.setRefId(0);
                    attachment.setRefTable("wall_tv_post");
                    attachment.setS3Filename(filePath);
                    
                    attachments.add(attachment);
                }
            }
            
            // 3. wall_tv_post 생성
            WallTvPost post = new WallTvPost();
            post.setAuthorName("리얼테크");
            post.setAuthorIp(AppUtils.getClientIpFromRequest(request));
            post.setThumbnailUrl(thumbnailUrl);
            post.setTitle(params.getTitle());
            post.setContent(params.getContent());
            post.setCreatedAt(AppUtils.getCurrentDateTime());
            post.setViews(0);
            
            WallTvPost savedPost = wallTvPostRepository.save(post);
            
            attachments.forEach(a -> {
                a.setRefId(savedPost.getPostId());
            });
            
            attachmentRepository.saveAll(attachments);
            
        } else if ("CeilingTvPost".equals(params.getEntity())) {
            // 1. 썸네일 파일 s3에 업로드
            String thumbnailUrl = s3Service.uploadFile(params.getThumbnail(), AppUtils.generateEditorPath("ceiling_tv_post"));
            
            
            // 2. 첨부 파일 s3에 업로드
            List<Attachment> attachments = new ArrayList<>();
            if (params.getAttachments() != null) {
                for (MultipartFile file : params.getAttachments()) {
                    String filePath = s3Service.uploadFile(file, AppUtils.generateAttachmentPath("ceiling_tv_post"));
                    
                    Attachment attachment = new Attachment();
                    attachment.setDisplayFilename(file.getOriginalFilename());
                    attachment.setFileSizeKb(AppUtils.getFileSizeInKB(file));
                    attachment.setRefId(0);
                    attachment.setRefTable("ceiling_tv_post");
                    attachment.setS3Filename(filePath);
                    
                    attachments.add(attachment);
                }
            }
            
            // 3. wall_tv_post 생성
            CeilingTvPost post = new CeilingTvPost();
            post.setAuthorName("리얼테크");
            post.setAuthorIp(AppUtils.getClientIpFromRequest(request));
            post.setThumbnailUrl(thumbnailUrl);
            post.setTitle(params.getTitle());
            post.setContent(params.getContent());
            post.setCreatedAt(AppUtils.getCurrentDateTime());
            post.setViews(0);
            
            CeilingTvPost savedPost = ceilingTvPostRepository.save(post);
            
            attachments.forEach(a -> {
                a.setRefId(savedPost.getPostId());
            });
            
            attachmentRepository.saveAll(attachments);
        } else {
            throw new IllegalArgumentException("지원하지 않는 엔터티입니다: " + params.getEntity());
        }
    }
    
}
