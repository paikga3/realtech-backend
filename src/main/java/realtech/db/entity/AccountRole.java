package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the account_role database table.
 * 
 */
@Entity
@Table(name="account_role")
@NamedQuery(name="AccountRole.findAll", query="SELECT a FROM AccountRole a")
public class AccountRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="role_name")
	private String roleName;

	public AccountRole() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}