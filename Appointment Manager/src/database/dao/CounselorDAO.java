
package database.dao;

import java.util.List;
import model.Counselor;

public interface CounselorDAO {

    public int addCounselor(Counselor counselor);

    public int updateCounselor(Counselor counselor);

    public int deleteCounselorById(int id);

    public Counselor getCounselorById(int id);

    public List<Counselor> getAllCounselors();
    
    public int getMaxCounselorID();
    
}
