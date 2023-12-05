package com.blacklight.blogger.views;

import com.blacklight.blogger.controllers.AuthorController;
import com.blacklight.blogger.exceptions.ApiRequestException;
import com.blacklight.blogger.services.AuthorDataService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@PageTitle("Create author account")
@Route("/loginPage")
public class SignUpAndInAuthorAccountView extends VerticalLayout {

    @Autowired
    private final AuthorController authorController;
    private final AuthorDataService authorDataService;
    public SignUpAndInAuthorAccountView(AuthorController authorController, @Value ("${api.base-url}") String baseURL, AuthorDataService authorDataService) {
        this.authorController = authorController;
        this.authorDataService = authorDataService;

        String  authorURL = baseURL + "/authors" ;
        TextField name = new TextField("Name");
        TextField password = new TextField("Password");

        Button createAuthorAccBtn = new Button("Sign Up", e -> createAuthorAccount(name.getValue(), password.getValue(), authorURL));
        Button loginAuthorAccBtn = new Button("Login", e -> loginAuthorAcc(name.getValue(), password.getValue(), authorURL));

        setHeightFull();

        VerticalLayout inputLayout = new VerticalLayout(name, password);
        inputLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout buttonLayout = new HorizontalLayout(createAuthorAccBtn, loginAuthorAccBtn);
        buttonLayout.setSpacing(true);

        add(new H1("Be an author!"), inputLayout, buttonLayout);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    void loginAuthorAcc(String name, String password, String authorURL) {
        String requestBody = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";

        try {
            WebClient.create(authorURL + "/loginAuthorAcc")
                    .post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            authorDataService.setAuthorName(authorController.getAuthorByName(name));
            RouteConfiguration.forSessionScope().setRoute("dashboard", AuthorDashboardView.class);
            getUI().ifPresent(ui -> ui.navigate("dashboard"));
        }
        catch (WebClientResponseException e) {
            ApiRequestException ex = e.getResponseBodyAs(ApiRequestException.class);
            Notification.show(ex.getMessage());
        }
    }

    void createAuthorAccount(String name, String password, String authorURL) {
        String requestBody = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";

        try {
            WebClient.create(authorURL + "/createAuthorAcc")
                    .post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .toBodilessEntity().block();
            Notification.show("Account successfully created!");

            loginAuthorAcc(name, password, authorURL);
        }
        catch (WebClientResponseException e) {
            ApiRequestException ex = e.getResponseBodyAs(ApiRequestException.class);
            Notification.show(ex.getMessage());
        }
    }
}
