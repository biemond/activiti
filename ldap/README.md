Activiti code                     
==============                     
             
LDAP module for Activiti works with AD, OpenLDAP etc with support for the Manage Tab of the explorer application  

ldap groups with admin and user as cn have security-role as type  
rest of the group are automatically jave assignment as type               

requires the following jars
activiti-engine-5.12.jar  
ldap-client-api-0.1.jar  
shared-all-0.9.18.jar  


Change the following in the activiti-explorer application
                                 

activiti-standalone-context.xml 
-----------------------------------    

    <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
      <property name="dataSource" ref="dataSource" />
      <property name="transactionManager" ref="transactionManager" />
      <property name="databaseSchemaUpdate" value="true" />
      <property name="jobExecutorActivate" value="true" />
      <property name="customFormTypes">
        <list>
          <bean class="org.activiti.explorer.form.UserFormType"/>
          <bean class="org.activiti.explorer.form.ProcessDefinitionFormType"/> 
          <bean class="org.activiti.explorer.form.MonthFormType"/>   
        </list>
      </property>
      <property name="customSessionFactories">
        <list>
          <bean class="org.activiti.ldap.LDAPUserManagerFactory">
            <constructor-arg ref="ldapConnectionParams" />
          </bean>
          <bean class="org.activiti.ldap.LDAPGroupManagerFactory">
            <constructor-arg ref="ldapConnectionParams" />
          </bean>
        </list>
      </property>
    </bean>
    
    <bean id="ldapConnectionParams"   class="org.activiti.ldap.LDAPConnectionParams">
       <property name="ldapServer"      value="192.168.80.159" />
       <property name="ldapPort"        value="389" />
       <property name="ldapUser"        value="CN=Administrator,CN=Users,DC=alfa,DC=local" />
       <property name="ldapPassword"    value="Welcome05" />
       <property name="ldapUserBase"    value="CN=Users,DC=alfa,DC=local" />
       <property name="ldapGroupBase"   value="CN=Users,DC=alfa,DC=local" />     
       <property name="ldapUserObject"  value="user" />
       <property name="ldapGroupObject" value="group" />     
    </bean>
    
    

activiti-ui-context.xml  
-----------------------

    <!-- User cache usage depends on environment, hence the factory approach -->
    <bean name="userCache" class="org.activiti.explorer.cache.UserCacheFactoryBean">
      <property name="environment" value="alfresco" />
    </bean>