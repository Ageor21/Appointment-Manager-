package model;

public class APTType extends DataBasic {

    private int id;
    private String description;

    public APTType() {
    }

    public APTType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final APTType other = (APTType) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
