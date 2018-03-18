package Bean;

import java.util.List;

/**
 * Created by qingliu on 3/18/18.
 */
public class GroupMessage {
    private List<String> groupMembers;
        private String messageContent;

        public List<String> getGroupMembers() {
            return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
