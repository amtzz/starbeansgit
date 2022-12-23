package com.example.application.views.stock;

import com.example.application.data.entity.Product;
import com.example.application.data.entity.StockItem;
import com.example.application.data.entity.Store;
import com.example.application.data.service.ProductService;
import com.example.application.data.service.StockItemService;
import com.example.application.data.service.StoreService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PageTitle("Inbound Products")
@Route(value = "stock/inbound", layout = MainLayout.class)
public class InboundStockView extends Div {
    private UnorderedList inboundList;

    private StockItem stockItem;
    private TextField skuField;
    private TextField amountField;
    private Store store;

    private final StockItemService stockItemService;
    private final ProductService productService;
    private final StoreService storeService;
    private List<StockItem> inboundItems;

    @Autowired
    public InboundStockView(StockItemService stockItemService, ProductService productService, StoreService storeService) {
        this.stockItemService = stockItemService;
        this.productService = productService;
        this.storeService = storeService;

        storeService.get(UUID.fromString("78484397-fe8d-4540-8712-a58dff9a2451"))
                .ifPresentOrElse(currentStore -> this.store = currentStore,
                () -> Notification.show("Store not found!!"));
        inboundItems = new LinkedList<>();

        addClassNames("checkout-form-view", "flex", "flex-col", "h-full");

        Main content = new Main();
        content.addClassNames("grid", "gap-xl", "items-start", "justify-center", "max-w-screen-md", "mx-auto", "pb-l",
                "px-l");

        content.add(createCheckoutForm());
        content.add(createAside());
        add(content);
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames("flex", "flex-col", "flex-grow");

        H2 header = new H2("Inbound Products");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        Paragraph note = new Paragraph("All fields are required unless otherwise noted");
        note.addClassNames("mb-xl", "mt-0", "text-secondary");
        checkoutForm.add(header, note);

        checkoutForm.add(createInboundProductSection());
//        checkoutForm.add(createShippingAddressSection());
//        checkoutForm.add(createPaymentInformationSection());
//        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private Section createInboundProductSection() {
        Section personalDetails = new Section();
        personalDetails.addClassNames("flex", "flex-col", "mb-xl", "mt-m");

        Paragraph stepOne = new Paragraph("Checkout 1/3");
        stepOne.addClassNames("m-0", "text-s", "text-secondary");

        H3 header = new H3("Product");
        header.addClassNames("mb-m", "mt-s", "text-2xl");

        skuField = new TextField("SKU");
        skuField.setRequiredIndicatorVisible(true);
        skuField.setPattern("[\\p{L}\\d\\-]+");
        skuField.addClassNames("mb-s");

        amountField = new TextField("Amount");
        amountField.setValue("1");
        amountField.setRequiredIndicatorVisible(true);
        amountField.setPattern("[\\d \\-\\+]+");
        amountField.addClassNames("mb-s");

        personalDetails.add(header, skuField, amountField);
        return personalDetails;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames("flex", "items-center", "justify-between", "my-m");

//        Button cancel = new Button("Cancel arrival");
//        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button addProduct = new Button("Add", new Icon(VaadinIcon.SIGN_IN));
        addProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        addProduct.addClickListener(buttonClickEvent -> addProductAction(skuField.getValue(), amountField.getValue()));

        footer.add(addProduct);
        return footer;
    }

    private void addProductAction(String sku, String amountValue) {
        UUID productId = UUID.fromString(sku);
        Optional<Product> productFound = productService.get(productId);
        productFound.ifPresentOrElse(product -> {
            final StockItem stockItem = new StockItem(store, product, Integer.parseInt(amountValue));
            inboundItems.add(stockItem);
            inboundList.add(createListItem(product.getName(), sku, amountValue));
        },
        () -> Notification.show(
                String.format("Product not found, SKU = %s", sku), 3000,
                Notification.Position.BOTTOM_START)
        );
    }

    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "sticky");
        Header headerSection = new Header();
        headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");
        H3 header = new H3("Order");
        header.addClassNames("m-0");
        Button edit = new Button("Edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);

        inboundList = createInboundList();

        aside.add(headerSection, inboundList, createAsideFooter());
        return aside;
    }

    private Footer createAsideFooter() {
        Footer footer = new Footer();
        footer.addClassNames("flex", "items-center", "justify-between", "my-m");

        Button cancel = new Button("Cancel Arrival");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button addProduct = new Button("Save Arrival", new Icon(VaadinIcon.SIGN_IN));
        addProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        addProduct.addClickListener(buttonClickEvent -> saveInboundListAction());

        footer.add(addProduct);
        return footer;
    }

    private void saveInboundListAction() {
        for (StockItem inboundItem : inboundItems) {
            stockItemService.addToStock(inboundItem);
        }
        Notification.show("Inbound List saved successfully!");
        clearList();
    }

    private void clearList() {
        inboundItems = new LinkedList<>();
        inboundList.removeAll();
    }




    private UnorderedList createInboundList() {
        UnorderedList ul = new UnorderedList();
        ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

        ul.add(createListItem("Gansito", "78484397-fe8d-4540-8712", "7"));
        ul.add(createListItem("Chocoroles", "78484397-fe8d-4540-8712", "80"));
        ul.add(createListItem("Pinguinos", "78484397-fe8d-4540-8712", "50"));
        return ul;
    }

    private ListItem createListItem(String primary, String secondary, String amount) {
        ListItem item = new ListItem();
        item.addClassNames("flex", "justify-between");

        Div subSection = new Div();
        subSection.addClassNames("flex", "flex-col");

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames("text-s text-secondary");
        subSection.add(secondarySpan);

        Span priceSpan = new Span(amount);

        item.add(subSection, priceSpan);
        return item;
    }
}
