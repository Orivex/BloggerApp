package com.blacklight.blogger.views;

import com.blacklight.blogger.entities.Author;
import com.blacklight.blogger.entities.Blog;
import com.blacklight.blogger.services.AuthorService;
import com.blacklight.blogger.services.BlogService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Route("/search")
public class BlogSearchView extends VerticalLayout {

    @Autowired
    AuthorService authorService;

    public BlogSearchView(@Value("${api.base-url}") String baseURL){

        String blogURL = baseURL + "/blog";

        setHeightFull();
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField searchForTitleField = new TextField("Author");

        searchForTitleField.setMaxLength(50);

        Button confirm_button = new Button("Search");
        List<Button> buttons = new ArrayList<>();

        confirm_button.addClickListener(clickEvent -> {

            for (Button button: buttons) {
                remove(button);
            }
            buttons.clear();

            Author author = authorService.getAuthorByName(searchForTitleField.getValue());

            List<Blog> blogs = author.getBlogs();

            for (int i = 0; i < blogs.size(); i++) {
                Long id = blogs.get(i).getId();

                buttons.add(new Button(blogs.get(i).getTitle()));

                buttons.get(i).addClickListener(
                        e -> getUI().ifPresent(ui -> ui.navigate(BlogView.class, new RouteParam("id", id)))
                );

                add(buttons.get(i));
            }
        });

        add(new H2("Find blogs of specific authors!"), searchForTitleField, confirm_button);
    }
}
