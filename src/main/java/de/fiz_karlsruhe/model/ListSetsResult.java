package de.fiz_karlsruhe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListSetsResult {

    private final List<Set> sets;
    private final String resumptionToken;    // null if no more pages
    private final int cursor;                // position of first record in this page
    private final Integer completeListSize;  // optional

    @JsonCreator
    public ListSetsResult(
            @JsonProperty("sets") List<Set> sets,
            @JsonProperty("resumptionToken") String resumptionToken,
            @JsonProperty("cursor") int cursor,
            @JsonProperty("completeListSize") Integer completeListSize) {
        this.sets = sets;
        this.resumptionToken = resumptionToken;
        this.cursor = cursor;
        this.completeListSize = completeListSize;
    }

    public List<Set> getSets() {
        return sets;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

    public int getCursor() {
        return cursor;
    }

    public Integer getCompleteListSize() {
        return completeListSize;
    }
}