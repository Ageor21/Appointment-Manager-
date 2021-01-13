
package database.dao;

import model.Address;

public interface AddressDAO {

    public int addAddress(Address address);

    public int updateAddress(Address address);

    public int deleteAddressById(int id);

    public Address getAddressById(int id);
    
    public int getMaxAddressID();
    
}
