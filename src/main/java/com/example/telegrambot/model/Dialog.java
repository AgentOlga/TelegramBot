package com.example.telegrambot.model;

import com.example.telegrambot.constants.StatusReport;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Сущность диалогов.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private User guestId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volunteer_id")
    private User volunteerId;

    @Column(name = "text_message")
    private String textMessage;

    @Column(name = "date_message")
    private LocalDate dateMessage;

    public Dialog(User guestId,
                  User volunteerId,
                  String textMessage,
                  LocalDate dateMessage) {
        this.guestId = guestId;
        this.volunteerId = volunteerId;
        this.textMessage = textMessage;
        this.dateMessage = dateMessage;
    }
}
