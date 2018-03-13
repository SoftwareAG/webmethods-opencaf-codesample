package caf.war.wm_opencaf_showcase.controls.caf_h;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.webmethods.caf.faces.annotations.ExpireWithPageFlow;
import com.webmethods.caf.faces.bean.BaseFacesBean;

@ManagedBean(name="dynamicIncludeBean")
@SessionScoped
@ExpireWithPageFlow
public class DynamicInclude extends BaseFacesBean {
    
    protected static String[] COLORS = new String[] {
        "red", "green", "blue"
    };

    public String getRandomColor() {
        return COLORS[(int) (Math.random() * 10) % COLORS.length];
    }
    
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
