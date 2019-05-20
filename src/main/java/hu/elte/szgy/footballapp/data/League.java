package hu.elte.szgy.footballapp.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="league")
@PrimaryKeyJoinColumn(name = "compId")
public class League extends Competition implements Serializable {
    private static final long serialVersionUID = 1L;

    public League(){
        super();
    }
    public League(int id, String name){
        super();
        setCompId(id);
        setName(name);
    }
}
