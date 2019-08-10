package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "document")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DocuLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    @NotNull
    private Contract contract;

    @NotNull
    @Column(name = "document_date")
    private LocalDate date;

    @NotNull
    @Column(name = "document_title")
    private String title;

    @NotNull
    private String url;

    public DocuLink(UUID id) {
        this.id = id;
    }

    public DocuLink(UUID id, Contract contract, LocalDate date, String title, String url) {
        this.id = id;
        this.contract = contract;
        this.date = date;
        this.title = title;
        this.url = url;
    }

    public DocuLink(Contract contract, LocalDate date, String title, String url) {
        this (null, contract, date, title, url);
    }
}
