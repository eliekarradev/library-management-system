package libraryMS.utils.constants;

public enum LogType {
    Requests("Requests"),
    Actions("Actions"),
    SEARCHES("Searches"),
    DETAILS("Details"),
    EXPORTS("Exports");
    private String type;

    LogType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
