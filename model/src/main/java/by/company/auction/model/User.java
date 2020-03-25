package by.company.auction.model;

import java.util.List;

public class User extends Base {
    private String email;
    private String password;
    private String username;
    private Role role;

    private Integer companyId;
    private List<Integer> bidIds;
    private List<Integer> messageIds;
    private List<Integer> lotIds;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Пользователь №" + super.toString() +
                ":\nemail - " + email +
                ", пароль - " + password +
                "\nимя - " + username +
                ", роль - " + role +
                "\nid компании - " + companyId +
                "\nid ставок - " + bidIds +
                "\nid сообщений - " + messageIds +
                "\nid лотов - " + lotIds +
                '.';
    }

    public User(String email, String password, String username, Role role, Integer companyId, List<Integer> bidIds, List<Integer> messageIds, List<Integer> lotIds) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.companyId = companyId;
        this.bidIds = bidIds;
        this.messageIds = messageIds;
        this.lotIds = lotIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getBidIds() {
        return bidIds;
    }

    public void setBidIds(List<Integer> bidIds) {
        this.bidIds = bidIds;
    }

    public List<Integer> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Integer> messageIds) {
        this.messageIds = messageIds;
    }

    public List<Integer> getLotIds() {
        return lotIds;
    }

    public void setLotIds(List<Integer> lotIds) {
        this.lotIds = lotIds;
    }
}
