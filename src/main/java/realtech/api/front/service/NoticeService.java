package realtech.api.front.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import realtech.api.common.exception.PostNotFoundException;
import realtech.api.common.model.PagedResponse;
import realtech.api.common.service.S3Service;
import realtech.api.front.model.CreateNoticeParams;
import realtech.api.front.model.FetchNoticesParams;
import realtech.api.front.model.FileItem;
import realtech.api.front.model.NoticeDetail;
import realtech.api.front.model.NoticePost;
import realtech.db.entity.Attachment;
import realtech.db.entity.Comment;
import realtech.db.entity.Notice;
import realtech.db.repository.AttachmentRepository;
import realtech.db.repository.CommentRepository;
import realtech.db.repository.NoticeRepository;
import realtech.util.AppUtil;

@Service
public class NoticeService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private S3Service s3Service;
    
    @Autowired
    private NoticeRepository noticeRepository;
    
    /**
     * 공지사항 리스트 가져오기
     * 
     * @param rq
     * @return
     */
    public PagedResponse<NoticePost> fetchNotices(FetchNoticesParams rq) {
        
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
        StringBuffer bodySelect = new StringBuffer(" SELECT new realtech.api.front.model.NoticePost(")
                .append("t.noticeId, t.title, t.authorName, t.createdAt) ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<NoticePost> listQuery = entityManager.createQuery(bodySelect.toString(), NoticePost.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        
        List<NoticePost> posts = listQuery.getResultList();
        for (NoticePost post : posts) {
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
        
        PagedResponse<NoticePost> pr = new PagedResponse<>();
        pr.setData(posts);
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
    
    public NoticeDetail getNoticeDetail(int id, boolean isUpdateViewCount) throws Exception {
        Notice post = noticeRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        
        // 조회 수 업데이트
        if (isUpdateViewCount) {
            post.setViews(post.getViews() + 1);
            noticeRepository.save(post);
        }
        
        NoticeDetail detail = new NoticeDetail();
        detail.setId(post.getNoticeId());
        detail.setAuthorIp(post.getAuthorIp());
        detail.setAuthorName(post.getAuthorName());
        detail.setTitle(post.getTitle());
        detail.setContent(post.getContent());
        detail.setViews(post.getViews());
        detail.setCreatedAt(AppUtil.formatToCompactDateTime(post.getCreatedAt()));
        
        // 첨부파일 조회
        List<FileItem> attachments = attachmentRepository.findByRefTableAndRefId("notice", post.getNoticeId())
                .stream()
                .map(f -> new FileItem(
                        f.getDisplayFilename(), 
                        f.getFileSizeKb().doubleValue(), 
                        "/api/attachments/download/" + f.getAttachmentId(),
                        UUID.randomUUID().toString(),
                        "done",
                        ""
                        )
                )
                .collect(Collectors.toList());
        detail.setAttachments(attachments);
        
        return detail;
    }
    
    
    public void createNotice(CreateNoticeParams params, HttpServletRequest request) throws Exception {

        List<Attachment> attachments = new ArrayList<>();
        if (params.getAttachments() != null) {
            for (MultipartFile file : params.getAttachments()) {
                String filePath = s3Service.uploadFile(file, AppUtil.generateAttachmentPath("notice"));
                
                Attachment attachment = new Attachment();
                attachment.setDisplayFilename(file.getOriginalFilename());
                attachment.setFileSizeKb(AppUtil.getFileSizeInKB(file));
                attachment.setRefId(0);
                attachment.setRefTable("notice");
                attachment.setS3Filename(filePath);
                
                attachments.add(attachment);
            }
        }
        
        Notice post = new Notice();
        post.setAuthorIp(AppUtil.getClientIpFromRequest(request));
        post.setAuthorName("리얼테크");
        post.setCreatedAt(AppUtil.getCurrentDateTime());
        post.setViews(0);
        post.setTitle(params.getTitle());
        post.setContent(params.getContent());
        
        Notice savedPost = noticeRepository.save(post);
        
        attachments.forEach(a -> {
            a.setRefId(savedPost.getNoticeId());
        });
        
        attachmentRepository.saveAll(attachments);
    }
    
    public void updateNotice(int id, CreateNoticeParams params, HttpServletRequest request) throws Exception {
        Optional<Notice> postOpt = noticeRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        Notice post = postOpt.get();
        post.setTitle(params.getTitle());
        post.setContent(params.getContent());
        post.setEditorName("리얼테크");
        post.setEditorIp(AppUtil.getClientIpFromRequest(request));
        post.setEditedAt(AppUtil.getCurrentDateTime());
        
        noticeRepository.save(post);
        
        // 기존 첨부파일 삭제
        List<Attachment> previousAttachments = attachmentRepository.findByRefTableAndRefId("notice", post.getNoticeId());
        for (Attachment attachment : previousAttachments) {
            s3Service.deleteFile(AppUtil.extractPathUsingString(attachment.getS3Filename()));
            attachmentRepository.delete(attachment);
        }
        
        
        // 신규 첨부파일 등록
        List<Attachment> attachments = new ArrayList<>();
        if (params.getAttachments() != null) {
            for (MultipartFile file : params.getAttachments()) {
                String filePath = s3Service.uploadFile(file, AppUtil.generateAttachmentPath("notice"));
                
                Attachment attachment = new Attachment();
                attachment.setDisplayFilename(file.getOriginalFilename());
                attachment.setFileSizeKb(AppUtil.getFileSizeInKB(file));
                attachment.setRefId(post.getNoticeId());
                attachment.setRefTable("notice");
                attachment.setS3Filename(filePath);
                
                attachments.add(attachment);
            }
        }
        if (!attachments.isEmpty()) {
            attachmentRepository.saveAll(attachments);
        }
    }
    
    public void deleteNotice(int id) {
        Optional<Notice> postOpt = noticeRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        Notice post = postOpt.get();
        // 기존 첨부파일 삭제
        List<Attachment> previousAttachments = attachmentRepository.findByRefTableAndRefId("notice", post.getNoticeId());
        for (Attachment attachment : previousAttachments) {
            // s3에서 첨부파일 삭제
            s3Service.deleteFile(AppUtil.extractPathUsingString(attachment.getS3Filename()));
            attachmentRepository.delete(attachment);
        }

        
        // 게시글 삭제
        noticeRepository.delete(post);
    }
    
    
}
