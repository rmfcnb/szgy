package hu.elte.szgy.footballapp.data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="cup")
@PrimaryKeyJoinColumn(name = "compId")
public class Cup extends Competition implements Serializable {
    private static final long serialVersionUID = 1L;

    public Cup(){
        super();
    }
    public Cup(int id, String name){
        super();
        setCompId(id);
        setName(name);
    }
}
