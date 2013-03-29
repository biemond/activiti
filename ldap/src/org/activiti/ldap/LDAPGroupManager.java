package org.activiti.ldap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.message.SearchResponse;
import org.apache.directory.ldap.client.api.message.SearchResultEntry;
import org.apache.directory.shared.ldap.cursor.Cursor;
import org.apache.directory.shared.ldap.entry.EntryAttribute;
import org.apache.directory.shared.ldap.filter.SearchScope;

public class LDAPGroupManager extends GroupEntityManager {


	private LDAPConnectionParams connectionParams;

	public LDAPGroupManager(LDAPConnectionParams params) {
		this.connectionParams = params;
	}

	@Override
	public Group createNewGroup(String groupId) {
		throw new ActivitiException(
				"LDAP group manager doesn't support creating a new group");
	}

	@Override
	public void insertGroup(Group group) {
		throw new ActivitiException(
				"LDAP group manager doesn't support inserting a new group");
	}



	@Override
	public void deleteGroup(String groupId) {
		throw new ActivitiException(
				"LDAP group manager doesn't support deleting a new group");
	}

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
System.out.println("findGroupByQueryCriteria");
		
		List<Group> groupList = new ArrayList<Group>();

		// Query is a GroupQueryImpl instance
		GroupQueryImpl groupQuery = (GroupQueryImpl) query;
		StringBuilder searchQuery = new StringBuilder();
		if (StringUtils.isNotEmpty(groupQuery.getId())) {
			searchQuery.append("(&(cn=").append(groupQuery.getId()).append(")(objectclass="+connectionParams.getLdapGroupObject()+"))");

		} else if (StringUtils.isNotEmpty(groupQuery.getName())) {
			searchQuery.append("(&(cn=").append(groupQuery.getName()).append(")(objectclass="+connectionParams.getLdapGroupObject()+"))");

		} else if (StringUtils.isNotEmpty(groupQuery.getUserId())) {
			searchQuery.append("(&(member= cn=").append(groupQuery.getUserId()).append("," + connectionParams.getLdapUserBase() + ")(objectclass="+connectionParams.getLdapGroupObject()+"))");

		} else {
			searchQuery.append("(&(cn=*)(objectclass="+connectionParams.getLdapGroupObject()+"))");
		}
System.out.println("searchQuery: "+searchQuery.toString());

		LdapConnection connection = LDAPConnectionUtil.openConnection(connectionParams);
		try {
			Cursor<SearchResponse> cursor = connection.search( connectionParams.getLdapGroupBase(),
															   searchQuery.toString(), 
															   SearchScope.ONELEVEL, 
															   "*");
			while (cursor.next()) {
				Group group = new GroupEntity();
				SearchResultEntry response = (SearchResultEntry) cursor.get();
//System.out.println("entry: "+response.toString());
				Iterator<EntryAttribute> itEntry = response.getEntry().iterator();
				while (itEntry.hasNext()) {
					EntryAttribute attribute = itEntry.next();
					String key = attribute.getId();
					if ("cn".equalsIgnoreCase(key)) {
//System.out.println("atrribute: "+attribute.getString());
						group.setId(attribute.getString());
						group.setName(attribute.getString());
						if ( attribute.getString().equalsIgnoreCase("user") || 
							 attribute.getString().equalsIgnoreCase("admin") ) {
							 group.setType("security-role");
						} else {
							 group.setType("assignment");
						}

					}
				}

				groupList.add(group);
			}

			cursor.close();

		} catch (Exception e) {
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return groupList;
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
System.out.println("findGroupCountByQueryCriteria");
		return findGroupByQueryCriteria(query, null).size();
	}

	@Override
	public GroupEntity findGroupById(String groupId) {

System.out.println("findGroupById: "+groupId);
        GroupEntity group = new GroupEntity();
		LdapConnection connection = LDAPConnectionUtil.openConnection(connectionParams);
		try {
			Cursor<SearchResponse> cursor = connection.search( connectionParams.getLdapGroupBase(),
					                                           "(&(cn=" + groupId + "," + connectionParams.getLdapUserBase() + ")(objectclass="+connectionParams.getLdapGroupObject()+"))", 
															   SearchScope.ONELEVEL, 
															   "*");
			while (cursor.next()) {
				SearchResultEntry response = (SearchResultEntry) cursor.get();
//System.out.println("entry: "+response.toString());
				Iterator<EntryAttribute> itEntry = response.getEntry().iterator();
				while (itEntry.hasNext()) {
					EntryAttribute attribute = itEntry.next();
					String key = attribute.getId();
					if ("cn".equalsIgnoreCase(key)) {
//System.out.println("atrribute: "+attribute.getString());
						group.setId(attribute.getString());
						group.setName(attribute.getString());
						if ( attribute.getString().equalsIgnoreCase("user") || 
							 attribute.getString().equalsIgnoreCase("admin") ) {
							 group.setType("security-role");
						} else {
							 group.setType("assignment");
						}

					}
				}
                break;
			}

			cursor.close();

		} catch (Exception e) {
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return group;
		
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
System.out.println("findGroupsByUser for user: "+userId);
		List<Group> groupList = new ArrayList<Group>();

		LdapConnection connection = LDAPConnectionUtil.openConnection(connectionParams);
		try {
System.out.println("search: "+"(member= cn=" + userId + "," + connectionParams.getLdapUserBase() + ")");

			Cursor<SearchResponse> cursor = 
					connection.search( connectionParams.getLdapGroupBase(),
					                   "(&(member= cn=" + userId + "," + connectionParams.getLdapUserBase() + ")(objectclass="+connectionParams.getLdapGroupObject()+"))",
					                   SearchScope.ONELEVEL,
					                   "*");
			while (cursor.next()) {
				Group group = new GroupEntity();
				SearchResultEntry response = (SearchResultEntry) cursor.get();
//System.out.println("entry: "+response.toString());
				Iterator<EntryAttribute> itEntry = response.getEntry()
						.iterator();
				while (itEntry.hasNext()) {
					EntryAttribute attribute = itEntry.next();
					String key = attribute.getId();
					if ("cn".equalsIgnoreCase(key)) {
//System.out.println("atrribute: "+attribute.getString());
						group.setId(attribute.getString());
						group.setName(attribute.getString());
						if ( attribute.getString().equalsIgnoreCase("user") || 
								 attribute.getString().equalsIgnoreCase("admin") ) {
								 group.setType("security-role");
						} else {
								 group.setType("assignment");
						}
					}
				}

				groupList.add(group);
			}

			cursor.close();

		} catch (Exception e) {
			throw new ActivitiException("LDAP connection search failure", e);
		}

		LDAPConnectionUtil.closeConnection(connection);

		return groupList;
	}
}