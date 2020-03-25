package by.company.auction.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Category extends Base{
    private String name;

    private List<Integer> lotIds;

    @Override
    public String toString() {
        return super.toString() + ". " + name;
    }

    public Category(String name, Integer... lotIds) {
        this.name = name;
        this.lotIds = new LinkedList<>(Arrays.asList(lotIds));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getLotIds() {
        return lotIds;
    }

    public void setLotIds(List<Integer> lotIds) {
        this.lotIds = lotIds;
    }
}
