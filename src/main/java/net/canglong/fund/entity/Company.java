package net.canglong.fund.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
    @Id
    private String id;
    private String name;
    private String abbr;
    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdOn;
    private String address;
}
