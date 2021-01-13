
package database.dao;

import java.util.List;
import model.APTType;

public interface APTtypeDAO {

    public int addAPTtype(APTType aptt);

    public int updateAPTtype(APTType aptt);

    public int deleteAPTtypeById(int id);

    public APTType getAPTtypeById(int id);
    
    public List<APTType> getAllAPTTypes();
    
    public int getMaxAPTtypeID();
    
}
