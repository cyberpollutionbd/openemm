/*********************************************************************************
 * The contents of this file are subject to the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.openemm.org/cpal1.html. The License is based on the Mozilla
 * Public License Version 1.1 but Sections 14 and 15 have been added to cover
 * use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenEMM.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is AGNITAS AG. All portions of
 * the code written by AGNITAS AG are Copyright (c) 2007 AGNITAS AG. All Rights
 * Reserved.
 * 
 * Contributor(s): AGNITAS AG. 
 ********************************************************************************/

package org.agnitas.taglib;


import java.util.List;
import java.util.ListIterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.agnitas.util.AgnUtils;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HibernateQuery extends BodyBase {
    
    private static final long serialVersionUID = -9049390953532060151L;
	// global variables:
    protected String query;
    protected String id=null;
    protected int startOffset=0;
    protected int maxRows=-1;
    protected int encodeHtml=1;
    
     /**
     * Setter for property query.
     * 
     * @param sql New value of property query.
     */
    public void setQuery(String sql) {
        query=new String(sql);
    }
     
    /**
     * Setter for property id.
     * 
     * @param aId New value of property id.
     */
    public void setId(String aId) {
        id=aId;
    }
    
     /**
     * Setter for property maxRows.
     * 
     * @param off New value of property maxRows.
     */
    public void setMaxRows(String off) {
        try {
            maxRows=Integer.parseInt(off);
        } catch (Exception e) {
            maxRows=-1;
        }
    }
    
    public int doStartTag() throws JspTagException {
        
        ApplicationContext aContext=WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());
        HibernateTemplate aTemplate=new HibernateTemplate((SessionFactory)aContext.getBean("sessionFactory"));
        List rset=null;
        
        if(id==null) {
            id=new String("");
        }
        
        try {
            startOffset=Integer.parseInt(pageContext.getRequest().getParameter("startWith"));
        } catch (Exception e) {
            startOffset=0;
        }
        
        pageContext.setAttribute("__"+id+"_MaxRows", new Integer(maxRows));
        
        try {
            rset=aTemplate.find(query);
            if(rset!=null && rset.size()>0) {
                ListIterator aIt=rset.listIterator(startOffset);
                pageContext.setAttribute("__"+id, rset);
                pageContext.setAttribute("__"+id+"_data", aIt);
                pageContext.setAttribute("__"+id+"_ShowTableRownum", new Integer(rset.size()));
                return doAfterBody();
            } else {
                return SKIP_BODY;
            }
            
        }   catch ( Exception e) {
            AgnUtils.logger().error("Exception: "+e);
            AgnUtils.logger().error(AgnUtils.getStackTrace(e));
            throw new JspTagException("Error: " + e);
        }
    }
    
    public int doAfterBody() throws JspException {
        ListIterator aIt=(ListIterator)pageContext.getAttribute("__"+id+"_data");
        Object aRecord=null;
        
        try {
            if(aIt.hasNext() && ((this.maxRows--)!=0)) {
                aRecord=aIt.next();
                pageContext.setAttribute(id, aRecord);
                return EVAL_BODY_BUFFERED;
            } else {
                return SKIP_BODY;
            }
        } catch (Exception e) {
            AgnUtils.logger().error(e);
        }
        return SKIP_BODY;
    }
    
}
