package org.activiti.ldap;

public class LDAPConnectionParams {
	private String ldapServer;
	private int ldapPort;
	private String ldapUser;
	private String ldapPassword;

	private String ldapGroupBase;
	private String ldapUserBase;

	private String ldapGroupObject;
	private String ldapUserObject;

	
	public String getLdapServer() {
		return ldapServer;
	}

	public void setLdapServer(String ldapServer) {
		this.ldapServer = ldapServer;
	}

	public int getLdapPort() {
		return ldapPort;
	}

	public void setLdapPort(int ldapPort) {
		this.ldapPort = ldapPort;
	}

	public String getLdapUser() {
		return ldapUser;
	}

	public void setLdapUser(String ldapUser) {
		this.ldapUser = ldapUser;
	}

	public String getLdapPassword() {
		return ldapPassword;
	}

	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	public String getLdapGroupBase() {
		return ldapGroupBase;
	}

	public void setLdapGroupBase(String ldapGroupBase) {
		this.ldapGroupBase = ldapGroupBase;
	}

	public String getLdapUserBase() {
		return ldapUserBase;
	}

	public void setLdapUserBase(String ldapUserBase) {
		this.ldapUserBase = ldapUserBase;
	}

	public String getLdapGroupObject() {
		return ldapGroupObject;
	}

	public void setLdapGroupObject(String ldapGroupObject) {
		this.ldapGroupObject = ldapGroupObject;
	}

	public String getLdapUserObject() {
		return ldapUserObject;
	}

	public void setLdapUserObject(String ldapUserObject) {
		this.ldapUserObject = ldapUserObject;
	}
}
