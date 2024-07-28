package filters;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/dashboard.xhtml")
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		String username = (session != null) ? (String) session.getAttribute("username") : null;
		String loginURL = request.getContextPath() + "/login.xhtml";

		if (username == null && !request.getRequestURI().equals(loginURL)) {
			response.sendRedirect(loginURL);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// If you have assigned any expensive resources as field of
		// this Filter class, then you could clean/close them here.
	}

}