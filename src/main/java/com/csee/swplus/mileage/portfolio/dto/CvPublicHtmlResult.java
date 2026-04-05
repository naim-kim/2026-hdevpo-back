package com.csee.swplus.mileage.portfolio.dto;

/**
 * Outcome of resolving public CV HTML by token (no JSON — controller maps to HTML responses).
 */
public final class CvPublicHtmlResult {

    public enum Kind {
        /** {@link #getHtmlBodyWhenOk()} is the saved CV HTML only. */
        OK,
        /** {@code is_public} is true but stored HTML is null/blank — HTTP 204, no body. */
        PUBLIC_EMPTY,
        /** CV exists but {@code is_public} is false — use 403 + fallback HTML. */
        PRIVATE,
        /** Unknown token. */
        NOT_FOUND,
        /** Token format invalid (not 8–12 digits). */
        INVALID_TOKEN
    }

    private final Kind kind;
    private final String htmlBodyWhenOk;

    private CvPublicHtmlResult(Kind kind, String htmlBodyWhenOk) {
        this.kind = kind;
        this.htmlBodyWhenOk = htmlBodyWhenOk;
    }

    public static CvPublicHtmlResult ok(String html) {
        return new CvPublicHtmlResult(Kind.OK, html != null ? html : "");
    }

    public static CvPublicHtmlResult publicEmpty() {
        return new CvPublicHtmlResult(Kind.PUBLIC_EMPTY, null);
    }

    public static CvPublicHtmlResult privateCv() {
        return new CvPublicHtmlResult(Kind.PRIVATE, null);
    }

    public static CvPublicHtmlResult notFound() {
        return new CvPublicHtmlResult(Kind.NOT_FOUND, null);
    }

    public static CvPublicHtmlResult invalidToken() {
        return new CvPublicHtmlResult(Kind.INVALID_TOKEN, null);
    }

    public Kind getKind() {
        return kind;
    }

    public String getHtmlBodyWhenOk() {
        return htmlBodyWhenOk;
    }
}
