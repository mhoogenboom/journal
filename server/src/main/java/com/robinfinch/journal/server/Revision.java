package com.robinfinch.journal.server;

/**
 * Contains versioning information for the sync.
 *
 * @author Mark Hoogenboom
 */
public class Revision {

    private long codeVersion;

    private long dataDefinitionVersion;

    private long dataVersion;

    public long getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(long codeVersion) {
        this.codeVersion = codeVersion;
    }

    public long getDataDefinitionVersion() {
        return dataDefinitionVersion;
    }

    public void setDataDefinitionVersion(long dataDefinitionVersion) {
        this.dataDefinitionVersion = dataDefinitionVersion;
    }

    public long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(long dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.app.rest.Revision[codeVersion=" + getCodeVersion()
                + ";dataDefinitionVersion=" + getDataDefinitionVersion()
                + ";dataVersion=" + getDataVersion()
                + "]";
    }
}
