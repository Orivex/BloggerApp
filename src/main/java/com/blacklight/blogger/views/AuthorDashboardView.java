package com.blacklight.blogger.views;

import com.blacklight.blogger.controllers.AuthorController;
import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.exceptions.ApiException;
import com.blacklight.blogger.exceptions.ApiRequestException;
import com.blacklight.blogger.services.AuthorDataService;
import com.blacklight.blogger.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PageTitle("Dashboard")
@Route(registerAtStartup = false)
public class AuthorDashboardView extends VerticalLayout implements BeforeLeaveObserver {

    private final AuthorController authorController;
    private final AuthorDataService authorDataService;
    Author author;
    public AuthorDashboardView(AuthorController authorController, AuthorDataService authorDataService,  @Value ("${api.base-url}") String baseURL) {
        this.authorController = authorController;
        this.authorDataService = authorDataService;

        author = authorDataService.getAuthor();

        String authorURL = baseURL + "/authors";

        setWidthFull();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        List<String> titles = listAuthorsTitles(authorURL);

        TextField titleField = new TextField("Title");
        TextArea textArea = new TextArea("Text");
        textArea.setWidth("10cm");
        Button postButton = new Button("Post", e -> {
            postBlog(authorURL, titleField.getValue(), textArea.getValue());
        });

        setPadding(true);

        add(new H1("Welcome home, " + author.getName()), new H3("Post a new blog"), titleField, textArea, postButton);
    }

    void postBlog(@Value("api.base-url") String authorURL, String title, String text) {

        String requestBody = "{\"title\":\"" + title + "\",\"text\":\"" + text + "\",\"date\":\"" + LocalDate.now() + "\",\"author\":"
                + "{\"id\":\"" + author.getId() + "\",\"name\":\"" + author.getName() + "\"}}";


        try {
            String response = WebClient.create(authorURL + "/postBlog/" + author.getId())
                    .post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Notification.show(response);
        }
        catch (WebClientResponseException e) {
            ApiRequestException ex = e.getResponseBodyAs(ApiRequestException.class);
            Notification.show(ex.toString());
        }
    }

    public List<String> listAuthorsTitles(@Value("api.base-url") String authorURL) {
        return WebClient.create(authorURL + "/getAuthorsTitles/" + author.getId())
                .get()
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();

    }

    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        RouteConfiguration.forSessionScope().removeRoute(AuthorDashboardView.class);
        //beforeLeaveEvent.rerouteTo("/blogger/authors");
    }
}
