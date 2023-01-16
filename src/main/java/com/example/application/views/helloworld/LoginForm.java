package com.example.application.views.helloworld;

import com.example.application.data.entity.UserData;
import com.example.application.data.service.SessionService;
import com.example.application.data.service.UserService;
import com.example.application.views.stock.StockView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.Optional;

/**
 * This is the default (and only) view in this example.
 * <p>
 * It demonstrates how to create a form using Vaadin and the Binder. The backend
 * service and data class are in the <code>.data</code> package.
 */
public class LoginForm extends VerticalLayout {

    private PasswordField passwordField;

    private final SessionService session;
    private final UserService userService;
    private BeanValidationBinder<UserData> binder;

    /**
     * Flag for disabling first run for password validation
     */
    private boolean enablePasswordValidation;

    /**
     * We use Spring to inject the backend into our view
     */
    public LoginForm(SessionService session, UserService userService) {

        this.session = session;
        this.userService = userService;

        /*
         * Create the components we'll need
         */

        H3 title = new H3("Login form");
        TextField username = new TextField("Username");
        passwordField = new PasswordField("Password");

        Span errorMessage = new Span();

        Button submitButton = new Button("Login");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        /*
         * Build the visible layout
         */

        // Create a FormLayout with all our components. The FormLayout doesn't have any
        // logic (validation, etc.), but it allows us to configure Responsiveness from
        // Java code and its defaults looks nicer than just using a VerticalLayout.
        FormLayout formLayout = new FormLayout(title, username, passwordField, errorMessage, submitButton);

        // Restrict maximum width and center on page
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        // Allow the form layout to be responsive. On device widths 0-490px we have one
        // column, then we have two. Field labels are always on top of the fields.
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        // These components take full width regardless if we use one column or two (it
        // just looks better that way)
        formLayout.setColspan(title, 2);
        formLayout.setColspan(errorMessage, 2);
        formLayout.setColspan(submitButton, 2);

        // Add some styles to the error message to make it pop out
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        // Add the form to the page
        add(formLayout);

        /*
         * Set up form functionality
         */

        /*
         * Binder is a form utility class provided by Vaadin. Here, we use a specialized
         * version to gain access to automatic Bean Validation (JSR-303). We provide our
         * data class so that the Binder can read the validation definitions on that
         * class and create appropriate validators. The BeanValidationBinder can
         * automatically validate all JSR-303 definitions, meaning we can concentrate on
         * custom things such as the passwords in this class.
         */
        binder = new BeanValidationBinder<>(UserData.class);

        // The handle has a custom validator, in addition to being required. Some values
        // are not allowed, such as 'admin'; this is checked in the validator.
        binder.forField(username)
//                .withValidator(this::validateHandle)
                .asRequired().bind("username");

        // Another custom validator, this time for passwords
        binder.forField(passwordField).asRequired()
//                .withValidator(this::passwordValidator)
                .bind("password");

        // A label where bean-level error messages go
        binder.setStatusLabel(errorMessage);

        // And finally the submit button
        submitButton.addClickListener(e -> {
            try {

                // Create empty bean to store the details into
                UserData detailsBean = new UserData();

                // Run validators and write the values to the bean
                binder.writeBean(detailsBean);

                // Call backend to store the data
                Optional<UserData> userOpt =
                        userService.findByUsername(detailsBean.getUsername(), detailsBean.getPassword());

                userOpt.ifPresentOrElse(user -> {
                    session.setUser(user);
                    UI.getCurrent().navigate(StockView.class);
                }, () -> errorMessage.setText("Wrong Login or Password"));

                // Show success message if everything went well
//                showSuccess(detailsBean);

            } catch (ValidationException e1) {
                // validation errors are already visible for each field,
                // and bean-level errors are shown in the status label.

                // We could show additional messages here if we want, do logging, etc.

            }
        });

        submitButton.addClickShortcut(Key.ENTER);

    }

    /**
     * We call this method when form submission has succeeded
     */
    private void showSuccess(UserData detailsBean) {
        Notification notification = Notification.show("Data saved, welcome " + detailsBean.getFirstName());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Here you'd typically redirect the user to another view
    }

    /**
     * Method to validate that:
     * <p>
     * 1) Password is at least 8 characters long
     * <p>
     * 2) Values in both fields match each other
     */
    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {

        /*
         * Just a simple length check. A real version should check for password
         * complexity as well!
         */
        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }

        if (!enablePasswordValidation) {
            // user hasn't visited the field yet, so don't validate just yet, but next time.
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

//        String pass2 = passwordField2.getValue();
//
//        if (pass1 != null && pass1.equals(pass2)) {
//            return ValidationResult.ok();
//        }

        return ValidationResult.error("Passwords do not match");
    }

//    /**
//     * Method that demonstrates using an external validator. Here we ask the backend
//     * if this handle is already in use.
//     */
//    private ValidationResult validateHandle(String handle, ValueContext ctx) {
//
//        String errorMsg = service.validateHandle(handle);
//
//        if (errorMsg == null) {
//            return ValidationResult.ok();
//        }
//
//        return ValidationResult.error(errorMsg);
//    }
//
//    /**
//     * Custom validator class that extends the built-in email validator.
//     * <p>
//     * Ths validator checks if the field is visible before performing the
//     * validation. This way, the validation is only performed when the user has told
//     * us they want marketing emails.
//     */
//    public class VisibilityEmailValidator extends EmailValidator {
//
//        public VisibilityEmailValidator(String errorMessage) {
//            super(errorMessage);
//        }
//
//        @Override
//        public ValidationResult apply(String value, ValueContext context) {
//
//            if (!allowMarketingBox.getValue()) {
//                // Component not visible, no validation
//                return ValidationResult.ok();
//            } else {
//                // normal email validation
//                return super.apply(value, context);
//            }
//        }
//    }
}
