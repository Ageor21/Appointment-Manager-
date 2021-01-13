package model;

public class Counselor extends DataBasic {
    
    private int id;
    private String name;
    private String password;
    private int pin;
    
    public Counselor() {
    }
    
    public Counselor(int id, String name, String password, int pin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.pin = pin;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getPin() {
        return pin;
    }
    
    public void setPin(int pin) {
        this.pin = pin;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Counselor other = (Counselor) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public boolean isAuthenticated(String username, String password, String pin) {
        return this.name.equals(username) && this.password.equals(password) && pin.equals(this.pin + "");
    }
    
}
