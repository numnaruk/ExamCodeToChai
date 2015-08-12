/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.gosoft.webcalendar.authen.ldap;

import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.log4j.Logger;

/**
 *
 * @author tanayudhthem
 */
public class LDAPConnector {

    public static String NAME = "name";
    public static String DEPARTMENT = "department";
    public static String COMPANY = "company";
    public static String MAIL = "mail";
    public static String MEMBEROF = "memberOf";
    public static String NameThai = "description";
    public static String ID = "employeeID";
    private static String returnedAtts[] = {NAME, DEPARTMENT, COMPANY, MAIL, MEMBEROF, NameThai, ID};
    //schema:CN=Description,CN=Schema,CN=Configuration,DC=cp,DC=co,DC=th
    private static String searchBase = "dc=7eleven,dc=cp,dc=co,dc=th";

    final private Logger log = Logger.getLogger(this.getClass().getName());

    public LDAPDataObject LdapConnection(String user, String password) {

        DirContext ctx;
        javax.naming.directory.BasicAttributes bAttrs;
        Hashtable env;
        LDAPDataObject lData = null;

        //String conserver = "ldap://10.151.18.47/";
        String conserver = "ldap://10.151.18.12/";
        String domain = "7eleven.cp.co.th";


        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, conserver);

        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain); // displsy name in 2kserver for internal login
        env.put(Context.SECURITY_CREDENTIALS, password); // password for internal login
        LdapContext ctxGC = null;
        try {

            String searchFilter = "(sAMAccountName=" + user + ")";
            SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctxGC = new InitialLdapContext(env, null);
            NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);

            SearchResult sr = (SearchResult) answer.next();
            Attributes attrs = sr.getAttributes();

            String key = "";
            Object value = "";
            if (attrs != null) {
                lData = new LDAPDataObject();
                NamingEnumeration ne = attrs.getAll();
                while (ne.hasMore()) {
                    Attribute attr = (Attribute) ne.next();
                    key = attr.getID();
                    if (LDAPDataObject.KEY_COMPANY.equalsIgnoreCase(key)) {
                        lData.setCompany(String.valueOf(attr.get()));
                    } else if (LDAPDataObject.KEY_DEPARTMENT.equalsIgnoreCase(key)) {
                        lData.setDepartment(String.valueOf(attr.get()));
                    } else if (LDAPDataObject.KEY_EMAIL.equalsIgnoreCase(key)) {
                        lData.setEmail(String.valueOf(attr.get()));
                    } else if (LDAPDataObject.KEY_EMPLOYEEID.equalsIgnoreCase(key)) {
                        lData.setEmployeeID("0000081");
                        //lData.setEmployeeID(String.valueOf(attr.get()));
                    } else if (LDAPDataObject.KEY_MEMBEROF.equalsIgnoreCase(key)) {
                        lData.setMemberOf(String.valueOf(attr.get()));
                        String[] group_ = lData.getMemberOf().split(",");
                        lData.setGroup_member(group_[0].substring(3, group_[0].length()));
                    } else if (LDAPDataObject.KEY_NAME.equalsIgnoreCase(key)) {
                        lData.setName(String.valueOf(attr.get()));
                    } else if (LDAPDataObject.KEY_NAME_THAI.equalsIgnoreCase(key)) {
                        lData.setNameThai(String.valueOf(attr.get()));
                    }
                    //  System.out.println("key:" + key + " value:" + attr.get());
                }
                return lData;
            } else {
                return lData;
            }

        } catch (AuthenticationException t) {
            //System.out.println("Error LDAP: error code 49 - 80090308 Username and Password Invalid ");
            log.error("Error LDAP: error code 49 - 80090308 Username and Password Invalid",t );
            return null;
        } catch (Throwable t) {
            log.error(t.toString(),t);
            return null;
        }
    }
}
