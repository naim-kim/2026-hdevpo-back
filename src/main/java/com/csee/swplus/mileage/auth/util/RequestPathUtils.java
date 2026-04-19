package com.csee.swplus.mileage.auth.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Servlet path helpers so filters match the same route regardless of {@code server.servlet.context-path}
 * (e.g. {@code /milestone25_1}, {@code /mileage}, {@code /naimkim_1}).
 */
public final class RequestPathUtils {

    private RequestPathUtils() {}

    /**
     * Request path after the context path — same shape as controller mappings ({@code /api/...}),
     * not the raw {@link HttpServletRequest#getRequestURI()} which includes the context prefix.
     */
    public static String pathWithinApplication(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null || uri.isEmpty()) {
            return "/";
        }
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            String path = uri.substring(contextPath.length());
            return path.isEmpty() ? "/" : path;
        }
        return uri;
    }
}
