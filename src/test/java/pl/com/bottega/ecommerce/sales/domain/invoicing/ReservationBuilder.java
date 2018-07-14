package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;

public class ReservationBuilder {

    private ReservationStatus status;
    private ClientData clientData;
    private Date createDate;

    public ReservationBuilder() {
        this.status = Reservation.ReservationStatus.OPENED;
        this.clientData = new ClientData(Id.generate(), "Jan Kowalski");
        this.createDate = new Date();
    }

    public ReservationBuilder withStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public ReservationBuilder withClientData(ClientData client) {
        this.clientData = client;
        return this;
    }

    public ReservationBuilder withCreationDate(Date date) {
        this.createDate = date;
        return this;
    }

    public Reservation build() {
        return new Reservation(Id.generate(), status, clientData, createDate);
    }
}
