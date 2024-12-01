package realtech.api.front.service;

import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import realtech.api.common.exception.PostNotFoundException;
import realtech.api.common.exception.UnauthorizedException;
import realtech.api.common.model.PagedResponse;
import realtech.api.common.service.S3Service;
import realtech.api.front.model.CreateReservationInquiryPostParams;
import realtech.api.front.model.FetchReservationInquiriesParams;
import realtech.api.front.model.FileItem;
import realtech.api.front.model.ReservationInquiryPost;
import realtech.api.front.model.ReservationInquiryPostDetail;
import realtech.db.entity.Attachment;
import realtech.db.entity.Comment;
import realtech.db.entity.ReservationInquiry;
import realtech.db.repository.AttachmentRepository;
import realtech.db.repository.CommentRepository;
import realtech.db.repository.ReservationInquiryRepository;
import realtech.util.AppUtil;

@Service
public class ReservationInquiryService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ReservationInquiryRepository reservationInquiryRepository;

    @Autowired
    private S3Service s3Service;
    
    public PagedResponse<ReservationInquiryPost> fetchReservationInquiries(FetchReservationInquiriesParams rq) {
        
        StringBuffer where = new StringBuffer();
        where.append(" from ReservationInquiry t ");
        where.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            where.append(" and t.content like concat('%',:searchKeyword,'%') ");
        }
        
        
        where.append(" order by t.inquiryId desc ");
        
        
        
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT new realtech.api.front.model.ReservationInquiryPost(")
                .append("t.inquiryId, t.name, t.contact, t.createdAt, CASE WHEN t.isPrivate = 1 THEN true ELSE false END) ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<ReservationInquiryPost> listQuery = entityManager.createQuery(bodySelect.toString(), ReservationInquiryPost.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        
        List<ReservationInquiryPost> posts = listQuery.getResultList();
        for (ReservationInquiryPost post : posts) {
            List<Comment> comments = commentRepository.findByRefTableAndRefId("reservation_inquiry", post.getId())
                    .stream()
                    .filter(a -> {
                        return a.getIsDeleted() == 0;
                    })
                    .collect(Collectors.toList());
            
            List<Comment> adminComments = comments
                    .stream()
                    .filter(a -> {
                        return "리얼테크".equals(a.getAuthorName());
                    })
                    .collect(Collectors.toList());
            List<Attachment> attachments = attachmentRepository.findByRefTableAndRefId("reservation_inquiry", post.getId());
            
            post.setCommentCount(comments.size());
            post.setHasAttachment(!attachments.isEmpty());
            post.setStatus(adminComments.isEmpty() ? "Pending" : "Answered");
            
            // 등록한지 10분 이내인 경우 신규글 마킹
            post.setNew(AppUtil.isNewPost(post.getCreatedAt()));
            
            
            String contact = post.getContact();
            StringBuffer dashContact = new StringBuffer();
            if (contact.length() == 11) {
                dashContact.append(contact.substring(0, 3))
                .append("-")
                .append(contact.substring(3, 7))
                .append("-")
                .append(contact.substring(7));
                
                post.setContact(dashContact.toString());
            } else if (contact.length() == 10) {
                dashContact.append(contact.substring(0, 3))
                .append("-")
                .append(contact.substring(3, 6))
                .append("-")
                .append(contact.substring(6));
                
                post.setContact(dashContact.toString());
            }
        }
        
        PagedResponse<ReservationInquiryPost> pr = new PagedResponse<>();
        pr.setData(posts);
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
    
    @Transactional
    public ReservationInquiryPostDetail getReservationInquiryDetail(int postId, boolean isUpdateViewCount) {
        ReservationInquiry post = reservationInquiryRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        
        // 조회 수 업데이트
        if (isUpdateViewCount) {
            post.setViews(post.getViews() + 1);
            reservationInquiryRepository.save(post);
        }
        
        
        ReservationInquiryPostDetail detail = new ReservationInquiryPostDetail();
        detail.setId(post.getInquiryId());
        detail.setAddressDetail(post.getAddressDetail());
        detail.setAuthorIp(post.getAuthorIp());
        detail.setBraketType(post.getBracketType());
        detail.setBraketTypeOther(post.getBraketTypeOther());
        detail.setContact(post.getContact());
        detail.setContent(post.getContent());
        detail.setCreatedAt(AppUtil.formatToCompactDateTime(post.getCreatedAt()));
        detail.setInstallationDate(AppUtil.convertToDashedFormat(post.getInstallationDate()));
        detail.setIsPrivate(post.getIsPrivate());
        detail.setLotAddress(post.getLotAddress());
        detail.setName(post.getName());
        detail.setPostalCode(post.getPostalCode());
        detail.setRoadAddress(post.getRoadAddress());
        detail.setSettopBoxEmbed(post.getSettopBoxEmbed());
        detail.setTvSize(post.getTvSize());
        detail.setTvSizeOther(post.getTvSizeOther());
        detail.setViews(post.getViews());
        detail.setWallType(post.getWallType());
        detail.setWallTypeOther(post.getWallTypeOther());
        
        // 첨부파일 조회
        List<FileItem> attachments = attachmentRepository.findByRefTableAndRefId("reservation_inquiry", postId)
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
    
    @Transactional
    public void createReservationInquiryPost(CreateReservationInquiryPostParams params, HttpServletRequest request) throws Exception {
        if ("리얼테크".equals(params.getName())) {
            throw new UnauthorizedException("관리자 이름으로 게시물을 등록할 수 없습니다.");
        }
        
        List<Attachment> attachments = new ArrayList<>();
        if (params.getAttachments() != null) {
            for (MultipartFile file : params.getAttachments()) {
                String filePath = s3Service.uploadFile(file, AppUtil.generateAttachmentPath("reservation_inquiry"));
                
                Attachment attachment = new Attachment();
                attachment.setDisplayFilename(file.getOriginalFilename());
                attachment.setFileSizeKb(AppUtil.getFileSizeInKB(file));
                attachment.setRefId(0);
                attachment.setRefTable("reservation_inquiry");
                attachment.setS3Filename(filePath);
                
                attachments.add(attachment);
            }
        }
        
        ReservationInquiry post = new ReservationInquiry();
        post.setName(params.getName());
        post.setContact(params.getContact());
        post.setContent(params.getContent());
        post.setPostalCode(params.getPostalCode());
        post.setLotAddress(params.getLotAddress());
        post.setRoadAddress(params.getRoadAddress());
        post.setAddressDetail(params.getAddressDetail());
        post.setInstallationDate(params.getInstallationDate());
        post.setTvSize(params.getTvSize());
        if ("Z".equals(params.getTvSize())) {
            post.setTvSizeOther(params.getTvSizeOther());
        }
        post.setWallType(params.getWallType());
        if ("Z".equals(params.getWallType())) {
            post.setWallTypeOther(params.getWallTypeOther());
        }
        post.setBracketType(params.getBraketType());
        if ("Z".equals(params.getBraketType())) {
            post.setBraketTypeOther(params.getBraketTypeOther());
        }
        post.setSettopBoxEmbed(params.getSettopBoxEmbed());
        
        // 솔트 생성
        byte[] salt = AppUtil.generateSalt();

        // 해싱
        String hashedPassword = AppUtil.hashPassword(params.getPassword(), salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        post.setSalt(saltBase64);
        post.setPassword(hashedPassword);
        post.setIsPrivate(1);
        post.setAuthorIp(AppUtil.getClientIpFromRequest(request));
        post.setCreatedAt(AppUtil.getCurrentDateTime());
        
        ReservationInquiry savedPost = reservationInquiryRepository.save(post);
        
        attachments.forEach(a -> {
            a.setRefId(savedPost.getInquiryId());
        });
        
        attachmentRepository.saveAll(attachments);
    }
    
    public void updateReservationInquiryPost(int id, CreateReservationInquiryPostParams params, HttpServletRequest request) throws Exception {
        Optional<ReservationInquiry> postOpt = reservationInquiryRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        ReservationInquiry post = postOpt.get();
        post.setName(params.getName());
        post.setContact(params.getContact());
        post.setContent(params.getContent());
        post.setPostalCode(params.getPostalCode());
        post.setLotAddress(params.getLotAddress());
        post.setRoadAddress(params.getRoadAddress());
        post.setAddressDetail(params.getAddressDetail());
        post.setInstallationDate(params.getInstallationDate());
        post.setTvSize(params.getTvSize());
        if ("Z".equals(params.getTvSize())) {
            post.setTvSizeOther(params.getTvSizeOther());
        }
        post.setWallType(params.getWallType());
        if ("Z".equals(params.getWallType())) {
            post.setWallTypeOther(params.getWallTypeOther());
        }
        post.setBracketType(params.getBraketType());
        if ("Z".equals(params.getBraketType())) {
            post.setBraketTypeOther(params.getBraketTypeOther());
        }
        post.setSettopBoxEmbed(params.getSettopBoxEmbed());
        
        if (StringUtils.isNotEmpty(params.getPassword())) {
            // 솔트 생성
            byte[] salt = AppUtil.generateSalt();

            // 해싱
            String hashedPassword = AppUtil.hashPassword(params.getPassword(), salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            post.setSalt(saltBase64);
            post.setPassword(hashedPassword);
        }
        
        // 수정내용 저장
        reservationInquiryRepository.save(post);
        
        // 기존 첨부파일 삭제
        List<Attachment> previousAttachments = attachmentRepository.findByRefTableAndRefId("reservation_inquiry", post.getInquiryId());
        for (Attachment attachment : previousAttachments) {
            s3Service.deleteFile(AppUtil.extractPathUsingString(attachment.getS3Filename()));
            attachmentRepository.delete(attachment);
        }
        
        
        // 신규 첨부파일 등록
        List<Attachment> attachments = new ArrayList<>();
        if (params.getAttachments() != null) {
            for (MultipartFile file : params.getAttachments()) {
                String filePath = s3Service.uploadFile(file, AppUtil.generateAttachmentPath("reservation_inquiry"));
                
                Attachment attachment = new Attachment();
                attachment.setDisplayFilename(file.getOriginalFilename());
                attachment.setFileSizeKb(AppUtil.getFileSizeInKB(file));
                attachment.setRefId(post.getInquiryId());
                attachment.setRefTable("reservation_inquiry");
                attachment.setS3Filename(filePath);
                
                attachments.add(attachment);
            }
        }
        if (!attachments.isEmpty()) {
            attachmentRepository.saveAll(attachments);
        }
    }
    
    public void deleteReservationInquiryPost(int id) {
        Optional<ReservationInquiry> postOpt = reservationInquiryRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new PostNotFoundException("ID가 " + id + "인 게시글을 찾을 수 없습니다.");
        }
        
        ReservationInquiry post = postOpt.get();
        // 기존 첨부파일 삭제
        List<Attachment> previousAttachments = attachmentRepository.findByRefTableAndRefId("reservation_inquiry", post.getInquiryId());
        for (Attachment attachment : previousAttachments) {
            // s3에서 첨부파일 삭제
            s3Service.deleteFile(AppUtil.extractPathUsingString(attachment.getS3Filename()));
            attachmentRepository.delete(attachment);
        }

        
        // 게시글 삭제
        reservationInquiryRepository.delete(post);
    }
    
    
    
}
