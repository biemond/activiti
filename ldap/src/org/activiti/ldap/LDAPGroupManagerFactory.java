package org.activiti.ldap;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

public class LDAPGroupManagerFactory implements SessionFactory {

	private LDAPConnectionParams connectionParams;

	public LDAPGroupManagerFactory(LDAPConnectionParams params) {
		this.connectionParams = params;
	}

	@Override
	public Class<?> getSessionType() {
		return GroupEntityManager.class;
	}

	@Override
	public Session openSession() {
		return new LDAPGroupManager(connectionParams);
	}

}
