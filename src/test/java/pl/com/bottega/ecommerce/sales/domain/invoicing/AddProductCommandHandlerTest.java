package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTest {

    private AddProductCommandHandler productHandler;
    private AddProductCommand addProductCommand;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private Reservation reservation;
    private ProductBuilder productBuilder;
    private AddProductCommandBuilder commandBuilder;
    private Product product;
    private Client client;

    @Before
    public void setUp() {
        clientRepository = mock(ClientRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        ClientData clientData = new ClientData(Id.generate(), "");
        client = new Client();
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, clientData, new Date());
        productBuilder = new ProductBuilder();
        product = productBuilder.withPrice(new Money(10)).withName("Dostepny").build();

        productHandler = new AddProductCommandHandler();
        commandBuilder = new AddProductCommandBuilder();
        addProductCommand = commandBuilder.withQuantity(6).build();

        when(clientRepository.load(any(Id.class))).thenReturn(client);
        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        Whitebox.setInternalState(productHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(productHandler, "productRepository", productRepository);
        Whitebox.setInternalState(productHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(productHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(productHandler, "systemContext", new SystemContext());
    }

    @Test
    public void handlingOneCommandShouldSaveReservationRepositoryOnce() {
        productHandler.handle(addProductCommand);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void handlingMultipleCommandsShouldSaveReservationRepositoryMultipleTimes() {
        productHandler.handle(addProductCommand);
        productHandler.handle(addProductCommand);
        productHandler.handle(addProductCommand);

        verify(reservationRepository, times(3)).save(reservation);
    }

    @Test
    public void reservationShouldStoreProductAfterHandlingIt() {
        productHandler.handle(addProductCommand);

        Assert.assertThat(reservation.contains(product), equalTo(true));
    }

    @Test
    public void handlingUnavailableProductShouldCallSuggestionService() {
        product.markAsRemoved();
        Product newProduct = productBuilder.ofType(ProductType.FOOD).build();
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(newProduct);

        productHandler.handle(addProductCommand);
        verify(suggestionService, times(1)).suggestEquivalent(product, client);
    }
}
