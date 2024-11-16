package realtech.db.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the account_role_map database table.
 * 
 */
@Entity
@Table(name="account_role_map")
@NamedQuery(name="AccountRoleMap.findAll", query="SELECT a FROM AccountRoleMap a")
public class AccountRoleMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="account_id")
	private int accountId;

	@Column(name="role_id")
	private int roleId;

	public AccountRoleMap() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return this.accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}