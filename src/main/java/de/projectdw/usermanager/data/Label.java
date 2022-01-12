package de.projectdw.usermanager.data;

import jdk.jfr.Enabled;
import lombok.Data;

import javax.persistence.*;

@Entity (name = "Label")
@Data
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Image image;

    private int x1 ;
    private int x2 ;
    private int y1 ;
    private int y2 ;

    public Label() {

    }

    public Label(Image image, int x1, int x2, int y1, int y2) {
        this.image = image;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
}
