package Item;

import java.io.Serializable;

public class Item implements Serializable{
    private long id;
    private String name;
    private float money;
    private String type;
    private String usetime;
    private String limitTime;
    private String buyDate;
    private String modifyDateTime;
    public Item() {
        super();
    }
    public Item(long id, String name, float money, String type, String usetime, String limitTime,
                String buyDate, String modifyDateTime) {
        super();
        this.id = id;
        this.name = name;
        this.money = money;
        this.type = type;
        this.usetime = usetime;
        this.limitTime = limitTime;
        this.buyDate = buyDate;
        this.modifyDateTime = modifyDateTime;
    }
    public Item(String name, float money, String type, String usetime, String limitTime,
                String buyDate, String modifyDateTime) {
        super();
        this.name = name;
        this.money = money;
        this.type = type;
        this.usetime = usetime;
        this.limitTime = limitTime;
        this.buyDate = buyDate;
        this.modifyDateTime = modifyDateTime;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getAge() {
        return money;
    }
    public void setAge(float money) {
        this.money = money;
    }
    public String getSex() {
        return type;     }
    public void setSex(String type) {
        this.type = type;
    }
    public String getLike() {
        return usetime;
    }
    public void setLike(String usetime) {
        this.usetime = usetime;
    }
    public String getLimitTime() {
        return limitTime;
    }
    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;     }
    public String getTrainDate() {
        return buyDate;
    }
    public void setTrainDate(String buyDate) {
        this.buyDate = buyDate;
    }
    public String getModifyDateTime() {
        return modifyDateTime;
    }
    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }
}

