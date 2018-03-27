package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class BookKeeperTest {
    InvoiceFactory invoiceFactory;
    InvoiceRequest invoiceRequest;
    TaxPolicy taxPolicy;
    BookKeeper bookKeeper;
    ProductData productData;
    
    @Before
    public void setUp() throws Exception {
        taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(5), "");
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(tax);
        ClientData client = mock(ClientData.class);
        invoiceRequest = new InvoiceRequest(client);
        productData = mock(ProductData.class);
        
        invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
    }

    @Test
    public void issuingInvoiceWithOnePositionShouldReturnInvoiceWithOnePosition() {
        RequestItem item = new RequestItem(productData, 4, new Money(5));
        invoiceRequest.add(item);

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        int positions = result.getItems().size();
        assertThat(positions, Matchers.equalTo(1));
    }
    @Test
    public void issuingInvoiceWithTwoPositionsShouldCallTaxPolicyMethodTwoTimesWithParametersMatchingInvoicePositions() {
        RequestItem item = new RequestItem(productData, 4, new Money(5));
        RequestItem secondItem = new RequestItem(productData, 2, new Money(4));
        invoiceRequest.add(item);
        invoiceRequest.add(secondItem);
        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, atMost(2)).calculateTax(any(ProductType.class), any(Money.class));
        verify(taxPolicy, times(1)).calculateTax(item.getProductData().getType(), item.getTotalCost());
        verify(taxPolicy, times(1)).calculateTax(secondItem.getProductData().getType(), secondItem.getTotalCost());
        }

}
