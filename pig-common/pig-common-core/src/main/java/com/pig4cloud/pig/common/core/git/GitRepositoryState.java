package com.pig4cloud.pig.common.core.git;

import lombok.Data;

import java.io.Serializable;
import java.util.Properties;

/**
 * git仓库状态
 *
 * @author lishangbu
 * @date 2021/10/10
 */
@Data
public class GitRepositoryState implements Serializable {
    private String tags;
    private String branch;
    private String dirty;
    private String remoteOriginUrl;

    private String commitId;
    private String commitIdAbbrev;
    private String describe;
    private String describeShort;
    private String commitUserName;
    private String commitUserEmail;
    private String commitMessageFull;
    private String commitMessageShort;
    private String commitTime;
    private String closestTagName;
    private String closestTagCommitCount;

    private String buildUserName;
    private String buildUserEmail;
    private String buildTime;
    private String buildHost;
    private String buildVersion;
    private String buildNumber;
    private String buildNumberUnique;

    private String totalCommitCount;

    public GitRepositoryState() {
    }

    public GitRepositoryState(Properties properties) {
        this.tags = properties.getProperty("git.tags", "");
        this.branch = properties.getProperty("git.branch", "");
        this.dirty = properties.getProperty("git.dirty", "");
        this.remoteOriginUrl = properties.getProperty("git.remote.origin.url", "");
        this.commitId = properties.getProperty("git.commit.id.full", "");
        this.commitIdAbbrev = properties.getProperty("git.commit.id.abbrev", "");
        this.describe = properties.getProperty("git.commit.id.describe", "");
        this.describeShort = properties.getProperty("git.commit.id.describe-short", "");
        this.commitUserName = properties.getProperty("git.commit.user.name", "");
        this.commitUserEmail = properties.getProperty("git.commit.user.email", "");
        this.commitMessageFull = properties.getProperty("git.commit.message.full", "");
        this.commitMessageShort = properties.getProperty("git.commit.message.short", "");
        this.commitTime = properties.getProperty("git.commit.time", "");
        this.closestTagName = properties.getProperty("git.closest.tag.name", "");
        this.closestTagCommitCount = properties.getProperty("git.closest.tag.commit.count", "");

        this.buildUserName = properties.getProperty("git.build.user.name", "");
        this.buildUserEmail = properties.getProperty("git.build.user.email", "");
        this.buildTime = properties.getProperty("git.build.time", "");
        this.buildHost = properties.getProperty("git.build.host", "");
        this.buildVersion = properties.getProperty("git.build.version", "");
        this.buildNumber = properties.getProperty("git.build.number", "");
        this.buildNumberUnique = properties.getProperty("git.build.number.unique", "");

        this.totalCommitCount = properties.getProperty("git.total.commit.count", "");
    }

}
