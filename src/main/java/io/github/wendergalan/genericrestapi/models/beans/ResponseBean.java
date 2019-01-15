package io.github.wendergalan.genericrestapi.models.beans;

import java.util.List;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: ResponseBean.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * This is the model that will be returned in
 * the method request to fetch all.
 * *********************************************
 *
 * @param <T> the type parameter
 */
public class ResponseBean<T> {

    private Integer currentPage;

    private Integer totalPages;

    private List<T> content;

    /**
     * Instantiates a new Response bean.
     *
     * @param currentPage the current page
     * @param totalPages  the total pages
     * @param content     the content
     */
    public ResponseBean(Integer currentPage, Integer totalPages, List<T> content) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.content = content;
    }

    /**
     * Gets current page.
     *
     * @return the current page
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets current page.
     *
     * @param currentPage the current page
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Gets total pages.
     *
     * @return the total pages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * Sets total pages.
     *
     * @param totalPages the total pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(List<T> content) {
        this.content = content;
    }
}
