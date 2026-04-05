package com.csee.swplus.mileage.portfolio.support;

/**
 * Builds minimal HTML responses for the public CV endpoint (structure in code, not pasted markup).
 */
public final class CvPublicHtmlFallbackPages {

    private static final String LANG = "ko";
    private static final String CHARSET_META = "<meta charset=\"UTF-8\">";
    private static final String VIEWPORT_META = "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">";

    private static final String TITLE_PRIVATE = "비공개 이력서";
    private static final String MSG_PRIVATE = "이 이력서는 비공개입니다.";

    private static final String TITLE_NOT_FOUND = "찾을 수 없음";
    private static final String MSG_NOT_FOUND = "이력서를 찾을 수 없습니다.";

    private CvPublicHtmlFallbackPages() {
    }

    public static String privateCvPage() {
        return minimalDocument(TITLE_PRIVATE, MSG_PRIVATE);
    }

    public static String notFoundPage() {
        return minimalDocument(TITLE_NOT_FOUND, MSG_NOT_FOUND);
    }

    private static String minimalDocument(String title, String bodyParagraph) {
        String safeTitle = escapeHtml(title);
        String safeBody = escapeHtml(bodyParagraph);
        StringBuilder sb = new StringBuilder(180 + safeTitle.length() + safeBody.length());
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"").append(LANG).append("\">");
        sb.append("<head>");
        sb.append(CHARSET_META);
        sb.append(VIEWPORT_META);
        sb.append("<title>").append(safeTitle).append("</title>");
        sb.append("</head><body>");
        sb.append("<p>").append(safeBody).append("</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String escapeHtml(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(raw.length() + 16);
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
