package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {

    private Money price;

    private String name;

    private ProductType productType;

    public ProductBuilder() {
        this.price = new Money(5);
        this.name = "produkt";
        this.productType = ProductType.STANDARD;
    }

    public ProductBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder ofType(ProductType type) {
        this.productType = type;
        return this;
    }

    public Product build() {
        return new Product(Id.generate(), price, name, productType);
    }
}
