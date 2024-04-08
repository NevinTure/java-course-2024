package edu.java.scrapper.model;

import edu.java.models.utils.State;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tg_chat")
public class TgChat {

    @Id
    private Long id;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private State state;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "tg_chat_link",
        joinColumns = @JoinColumn(name = "tg_chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private List<Link> linkList;

    public TgChat(Long id) {
        this.id = id;
        this.state = State.DEFAULT;
        linkList = new ArrayList<>();
    }
}
