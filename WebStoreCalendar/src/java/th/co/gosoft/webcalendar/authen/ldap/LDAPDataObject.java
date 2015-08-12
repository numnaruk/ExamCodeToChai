/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.gosoft.webcalendar.authen.ldap;

/**
 *
 * @author tirapongboo
 */
public class LDAPDataObject {

    public static final String KEY_COMPANY = "company";
    public static final String KEY_EMAIL = "mail";
    public static final String KEY_NAME = "name";
    public static final String KEY_DEPARTMENT = "department";
    public static final String KEY_EMPLOYEEID = "employeeID";
    public static final String KEY_NAME_THAI = "description";
    public static final String KEY_MEMBEROF = "memberOf";
    private String company;
    private String email;
    private String name;
    private String department;
    private String employeeID;
    private String nameThai;
    private String memberOf;
    private String group_member;
    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the employeeID
     */
    public String getEmployeeID() {
        return employeeID;
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * @return the nameThai
     */
    public String getNameThai() {
        return nameThai;
    }

    /**
     * @param nameThai the nameThai to set
     */
    public void setNameThai(String nameThai) {
        this.nameThai = nameThai;
    }

    /**
     * @return the memberOf
     */
    public String getMemberOf() {
        return memberOf;
    }

    /**
     * @param memberOf the memberOf to set
     */
    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    /**
     * @return the group_member
     */
    public String getGroup_member() {
        return group_member;
    }

    /**
     * @param group_member the group_member to set
     */
    public void setGroup_member(String group_member) {
        this.group_member = group_member;
    }

 
}
