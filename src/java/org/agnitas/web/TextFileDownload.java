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

package org.agnitas.web;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TextFileDownload extends HttpServlet {

    private static final long serialVersionUID = 5844323149267914354L;

	/**
     * Gets parameters.
     * reads file.
     * prints outputstream.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse response)
                      throws IOException, ServletException {

        response.setContentType("text/plain");
        Hashtable map;
        map = (Hashtable)(req.getSession().getAttribute("map"));       
      
        String outFileName = "";	// contains the Filename, build from the timestamp
        String outFile = "";		// contains the actual data.
      	
        outFileName = (String) req.getParameter("key");       
        outFile = (String)map.get(req.getParameter("key"));	// get the key from the Hashmap.

        // build filepath (timestamp + .csv) and return it.
        response.setHeader("Content-Disposition", "attachment; filename=\"" + outFileName + ".csv\";");
        ServletOutputStream ostream = response.getOutputStream();
        ostream.print(outFile);
    }
}
