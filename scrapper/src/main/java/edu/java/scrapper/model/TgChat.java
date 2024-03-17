package edu.java.scrapper.model;

import edu.java.models.utils.State;
import java.util.ArrayList;
import java.util.List;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
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
    @ManyToMany
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
