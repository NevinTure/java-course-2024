/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.model.jooq.tables.pojos;

import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GitRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long linkId;
    private String urn;
    private OffsetDateTime lastCheckAt;
    private OffsetDateTime lastUpdateAt;
    private OffsetDateTime lastPushAt;

    public GitRepository() {}

    public GitRepository(GitRepository value) {
        this.id = value.id;
        this.linkId = value.linkId;
        this.urn = value.urn;
        this.lastCheckAt = value.lastCheckAt;
        this.lastUpdateAt = value.lastUpdateAt;
        this.lastPushAt = value.lastPushAt;
    }

    @ConstructorProperties({ "id", "linkId", "urn", "lastCheckAt", "lastUpdateAt", "lastPushAt" })
    public GitRepository(
        @Nullable Long id,
        @Nullable Long linkId,
        @NotNull String urn,
        @NotNull OffsetDateTime lastCheckAt,
        @NotNull OffsetDateTime lastUpdateAt,
        @NotNull OffsetDateTime lastPushAt
    ) {
        this.id = id;
        this.linkId = linkId;
        this.urn = urn;
        this.lastCheckAt = lastCheckAt;
        this.lastUpdateAt = lastUpdateAt;
        this.lastPushAt = lastPushAt;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.ID</code>.
     */
    @Nullable
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.ID</code>.
     */
    public void setId(@Nullable Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.URN</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrn() {
        return this.urn;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.URN</code>.
     */
    public void setUrn(@NotNull String urn) {
        this.urn = urn;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.LAST_CHECK_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastCheckAt() {
        return this.lastCheckAt;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.LAST_CHECK_AT</code>.
     */
    public void setLastCheckAt(@NotNull OffsetDateTime lastCheckAt) {
        this.lastCheckAt = lastCheckAt;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.LAST_UPDATE_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastUpdateAt() {
        return this.lastUpdateAt;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.LAST_UPDATE_AT</code>.
     */
    public void setLastUpdateAt(@NotNull OffsetDateTime lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    /**
     * Getter for <code>GIT_REPOSITORY.LAST_PUSH_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastPushAt() {
        return this.lastPushAt;
    }

    /**
     * Setter for <code>GIT_REPOSITORY.LAST_PUSH_AT</code>.
     */
    public void setLastPushAt(@NotNull OffsetDateTime lastPushAt) {
        this.lastPushAt = lastPushAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GitRepository other = (GitRepository) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        if (this.urn == null) {
            if (other.urn != null)
                return false;
        }
        else if (!this.urn.equals(other.urn))
            return false;
        if (this.lastCheckAt == null) {
            if (other.lastCheckAt != null)
                return false;
        }
        else if (!this.lastCheckAt.equals(other.lastCheckAt))
            return false;
        if (this.lastUpdateAt == null) {
            if (other.lastUpdateAt != null)
                return false;
        }
        else if (!this.lastUpdateAt.equals(other.lastUpdateAt))
            return false;
        if (this.lastPushAt == null) {
            if (other.lastPushAt != null)
                return false;
        }
        else if (!this.lastPushAt.equals(other.lastPushAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.urn == null) ? 0 : this.urn.hashCode());
        result = prime * result + ((this.lastCheckAt == null) ? 0 : this.lastCheckAt.hashCode());
        result = prime * result + ((this.lastUpdateAt == null) ? 0 : this.lastUpdateAt.hashCode());
        result = prime * result + ((this.lastPushAt == null) ? 0 : this.lastPushAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GitRepository (");

        sb.append(id);
        sb.append(", ").append(linkId);
        sb.append(", ").append(urn);
        sb.append(", ").append(lastCheckAt);
        sb.append(", ").append(lastUpdateAt);
        sb.append(", ").append(lastPushAt);

        sb.append(")");
        return sb.toString();
    }
}