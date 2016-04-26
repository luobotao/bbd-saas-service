package org.springframework.csrf;

import java.util.List;
import java.util.regex.Pattern;
 
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.security.web.util.matcher.RequestMatcher;
 
/**
 * 版权：zuowenhai新石器时代<br/>
 * 作者：zuowenhai@neolix.cn <br/>
 * 生成日期：2016-04-26 <br/>
 * 描述：不被csrf过滤处理的url
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
     private Pattern allowedMethods = Pattern
             .compile("^(GET|HEAD|TRACE|OPTIONS)$");
 
     public boolean matches(HttpServletRequest request) {
 
         if (execludeUrls != null && execludeUrls.size() > 0) {
             String servletPath = request.getServletPath();
             for (String url : execludeUrls) {
                 if (servletPath.contains(url)) {
                     return false;
                 }
             }
         }
         return !allowedMethods.matcher(request.getMethod()).matches();
     }
 
     /**
      * 需要排除的url列表
      */
     private List<String> execludeUrls;
 
     public List<String> getExecludeUrls() {
         return execludeUrls;
     }
 
     public void setExecludeUrls(List<String> execludeUrls) {
         this.execludeUrls = execludeUrls;
     }
}
