package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the website_meta database table.
 * 
 */
@Entity
@Table(name="website_meta")
@NamedQuery(name="WebsiteMeta.findAll", query="SELECT w FROM WebsiteMeta w")
public class WebsiteMeta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Column(name="naver_key")
	private String naverKey;

	@Lob
	@Column(name="og_description")
	private String ogDescription;

	@Column(name="og_image")
	private String ogImage;

	@Column(name="og_title")
	private String ogTitle;

	@Column(name="og_url")
	private String ogUrl;

	@Column(name="page_title")
	private String pageTitle;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	public WebsiteMeta() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getNaverKey() {
		return this.naverKey;
	}

	public void setNaverKey(String naverKey) {
		this.naverKey = naverKey;
	}

	public String getOgDescription() {
		return this.ogDescription;
	}

	public void setOgDescription(String ogDescription) {
		this.ogDescription = ogDescription;
	}

	public String getOgImage() {
		return this.ogImage;
	}

	public void setOgImage(String ogImage) {
		this.ogImage = ogImage;
	}

	public String getOgTitle() {
		return this.ogTitle;
	}

	public void setOgTitle(String ogTitle) {
		this.ogTitle = ogTitle;
	}

	public String getOgUrl() {
		return this.ogUrl;
	}

	public void setOgUrl(String ogUrl) {
		this.ogUrl = ogUrl;
	}

	public String getPageTitle() {
		return this.pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}