package org.activiti.ldap;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

public class LDAPUserManagerFactory implements SessionFactory {

	private LDAPConnectionParams connectionParams;

	public LDAPUserManagerFactory(LDAPConnectionParams params) {
		this.connectionParams = params;
	}

	@Override
	public Class<?> getSessionType() {
		return UserEntityManager.class;
	}

	@Override
	public Session openSession() {
		return new LDAPUserManager(connectionParams);
	}

}