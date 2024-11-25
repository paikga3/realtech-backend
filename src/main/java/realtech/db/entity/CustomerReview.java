package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the customer_review database table.
 * 
 */
@Entity
@Table(name="customer_review")
@NamedQuery(name="CustomerReview.findAll", query="SELECT c FROM CustomerReview c")
public class CustomerReview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="review_id")
	private int reviewId;

	@Column(name="author_ip")
	private String authorIp;

	@Column(name="author_name")
	private String authorName;

	@Lob
	private String content;

	@Column(name="created_at")
	private String createdAt;

	@Column(name="edited_at")
	private String editedAt;

	@Column(name="editor_ip")
	private String editorIp;

	@Column(name="editor_name")
	private String editorName;

	@Column(name="is_private")
	private int isPrivate;

	private String password;

	private String salt;

	private String title;

	private int views;

	public CustomerReview() {
	}

	public int getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getAuthorIp() {
		return this.authorIp;
	}

	public void setAuthorIp(String authorIp) {
		this.authorIp = authorIp;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getEditedAt() {
		return this.editedAt;
	}

	public void setEditedAt(String editedAt) {
		this.editedAt = editedAt;
	}

	public String getEditorIp() {
		return this.editorIp;
	}

	public void setEditorIp(String editorIp) {
		this.editorIp = editorIp;
	}

	public String getEditorName() {
		return this.editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public int getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getViews() {
		return this.views;
	}

	public void setViews(int views) {
		this.views = views;
	}

}