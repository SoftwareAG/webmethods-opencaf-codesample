package caf.war.wm_opencaf_showcase.showcase;

import java.io.Writer;

import javax.faces.context.ExternalContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.webmethods.caf.common.StringTools;
import com.webmethods.caf.faces.bean.portlet.PortletAction;
import com.webmethods.caf.portlet.IPortletURL;

/**
 * Page Bean for the showcase test portlet
 */
public class ShowcaseDefaultviewView  extends   com.webmethods.caf.faces.bean.BasePageBean {
	private static final long serialVersionUID = 1L;
	private static final String[][] INITIALIZE_PROPERTY_BINDINGS = new String[][] {
	};
	private transient caf.war.wm_opencaf_showcase.showcase.Showcase showcase = null;

	/**
	 * Initialize page
	 */
	public String initialize() {
		try {
		    resolveDataBinding(INITIALIZE_PROPERTY_BINDINGS, null, "initialize", true, false);

		} catch (Exception e) {
			error(e);
			log(e);
		}	
		return null;
	}

	public caf.war.wm_opencaf_showcase.showcase.Showcase getShowcase()  {
		if (showcase == null) {
		    showcase = (caf.war.wm_opencaf_showcase.showcase.Showcase)resolveExpression("#{Showcase}");
		}
		return showcase;
	}
	
	public String getSwitchPageURL() {
		try {
			IPortletURL createActionUrl = createActionUrl();
			createActionUrl.clearParameters();
			createActionUrl.clearState();
			
			createActionUrl.setAXSRFT(false);
			createActionUrl.setTargetAction("switchPage");
			createActionUrl.setParameter("targetPage", "/test.xhtml");
			return createActionUrl.toString();
		} catch (Exception e) {
			error(e);
		}
		return "uh, oh..";
	}
	@PortletAction(axsrft=false)
	public String switchPage() {
		resetPageFlowStorage();
		String targetPage = (String)getRequestParam().get("targetPage");
		if (StringTools.notEmpty(targetPage)) {
			try {
				getShowcase().setTargetSample(StringTools.ensureLeading(targetPage, "/"));
				//getShowcase().storePreferences();
			} catch (Exception e) {
				error(e);
			}
		}
		//skip the rest of the lifecycle, go right to render
		getFacesContext().renderResponse();
		return null;
	}
	
	public boolean isInPortletRequest() {
		return getFacesContext().getExternalContext().getRequest() instanceof PortletRequest;
	}
	
	/**
	 * Portlet action handler that reads uploaded files and returns
	 * a JSON response containg the filenames that were uploaded.
	 */
	@PortletAction
	public String doFileUpload() {
		try {
	        ExternalContext externalContext = getFacesContext().getExternalContext();

	        FileItem fileItem = (FileItem)resolveExpression("#{inputFileBean.value}");
	        
//	        @SuppressWarnings("unchecked")
//			List<FileItem> files = (List<FileItem>) externalContext.getRequestMap().get(MultipartUtils.FILE_ITEMS);
	        
	        //process and write the JSON response.
			externalContext.setResponseContentType("application/json");
			externalContext.setResponseStatus(HttpServletResponse.SC_OK);
			externalContext.setResponseCharacterEncoding("UTF-8");
	        Writer responseOutputWriter = externalContext.getResponseOutputWriter();
	        responseOutputWriter.append("[");
	        
	        if (fileItem != null) {
	        	responseOutputWriter.append("\"")
        		.append(fileItem.getName())
        		.append("\"");
	        }
//	        Iterator<FileItem> iterator = files.iterator();
//	        while (iterator.hasNext()) {
//	        	FileItem next = iterator.next();
//	        	responseOutputWriter.append("\"")
//	        		.append(next.getName())
//	        		.append("\"");
//	        	if (iterator.hasNext()) {
//	        		responseOutputWriter.append(",\r\n");
//	        	}
//	        }
	        responseOutputWriter.append("]");
	        
	        //signal that the response was handled
	        getFacesContext().responseComplete();
		} catch (Exception e) {
			error(e);
		}
		return null;
	}

	/**
	 * Returns the href for an action url that will execute the 
	 * "doFileUpload" target action when the link is clicked.
	 */
	public String getFileUploadHref() {
		try {
			IPortletURL actionUrl = createActionUrl();
			actionUrl.clearParameters();
			actionUrl.clearState();
			
			actionUrl.setPortlet(getShowcase().getPortletURI());
			actionUrl.setTargetAction("doFileUpload");
			
			return actionUrl.toString();
		} catch (Exception e) {
			error(e);
		}
		return null;
	}
}