package realtech.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the reservation_inquiry database table.
 * 
 */
@Entity
@Table(name="reservation_inquiry")
@NamedQuery(name="ReservationInquiry.findAll", query="SELECT r FROM ReservationInquiry r")
public class ReservationInquiry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="inquiry_id")
	private int inquiryId;

	@Column(name="address_detail")
	private String addressDetail;

	@Column(name="author_ip")
	private String authorIp;

	@Column(name="bracket_type")
	private String bracketType;

	@Column(name="braket_type_other")
	private String braketTypeOther;

	private String contact;

	private String content;

	@Column(name="created_at")
	private String createdAt;

	@Column(name="installation_date")
	private String installationDate;

	@Column(name="is_private")
	private int isPrivate;

	@Column(name="lot_address")
	private String lotAddress;

	private String name;

	private String password;

	@Column(name="postal_code")
	private String postalCode;

	@Column(name="road_address")
	private String roadAddress;

	@Column(name="settop_box_embed")
	private int settopBoxEmbed;

	@Column(name="tv_size")
	private String tvSize;

	@Column(name="tv_size_other")
	private String tvSizeOther;

	private int views;

	@Column(name="wall_type")
	private String wallType;

	@Column(name="wall_type_other")
	private String wallTypeOther;
	
	public ReservationInquiry() {
	}

	public int getInquiryId() {
		return this.inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}

	public String getAddressDetail() {
		return this.addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getAuthorIp() {
		return this.authorIp;
	}

	public void setAuthorIp(String authorIp) {
		this.authorIp = authorIp;
	}

	public String getBracketType() {
		return this.bracketType;
	}

	public void setBracketType(String bracketType) {
		this.bracketType = bracketType;
	}

	public String getBraketTypeOther() {
		return this.braketTypeOther;
	}

	public void setBraketTypeOther(String braketTypeOther) {
		this.braketTypeOther = braketTypeOther;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getInstallationDate() {
		return this.installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public int getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(int isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getLotAddress() {
		return this.lotAddress;
	}

	public void setLotAddress(String lotAddress) {
		this.lotAddress = lotAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getRoadAddress() {
		return this.roadAddress;
	}

	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

	public int getSettopBoxEmbed() {
		return this.settopBoxEmbed;
	}

	public void setSettopBoxEmbed(int settopBoxEmbed) {
		this.settopBoxEmbed = settopBoxEmbed;
	}

	public String getTvSize() {
		return this.tvSize;
	}

	public void setTvSize(String tvSize) {
		this.tvSize = tvSize;
	}

	public String getTvSizeOther() {
		return this.tvSizeOther;
	}

	public void setTvSizeOther(String tvSizeOther) {
		this.tvSizeOther = tvSizeOther;
	}

	public int getViews() {
		return this.views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getWallType() {
		return this.wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public String getWallTypeOther() {
		return this.wallTypeOther;
	}

	public void setWallTypeOther(String wallTypeOther) {
		this.wallTypeOther = wallTypeOther;
	}

}