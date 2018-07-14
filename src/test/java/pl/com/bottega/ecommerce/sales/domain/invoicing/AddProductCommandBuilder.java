package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;

public class AddProductCommandBuilder {

    private Id orderId;
    private Id productId;
    private int quantity;

    public AddProductCommandBuilder() {
        this.orderId = Id.generate();
        this.productId = Id.generate();
        this.quantity = 1;
    }

    public AddProductCommandBuilder withOrder(Id orderId) {
        this.orderId = orderId;
        return this;
    }

    public AddProductCommandBuilder withProduct(Id productId) {
        this.productId = productId;
        return this;
    }

    public AddProductCommandBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public AddProductCommand build() {
        return new AddProductCommand(orderId, productId, quantity);
    }
}
