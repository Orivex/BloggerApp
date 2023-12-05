package com.blacklight.blogger.views;

import com.blacklight.blogger.controllers.BlogController;
import com.blacklight.blogger.entities.Blog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Route("/blog/:id")
public class BlogView extends VerticalLayout implements BeforeEnterObserver {

    private Long id;

    @Autowired
    private BlogController blogController;
    H1 title = new H1("No info");
    H5 author = new H5("No info");
    H5 date = new H5("No info");
    Text text = new Text("No info");

    public BlogView() {
        setHeightFull();
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        Div textWrapper = new Div(text);
        textWrapper.getStyle().set("width", "35cm");
        textWrapper.getStyle().set("margin", "0 auto"); // Center the text

        add(date, author, title, textWrapper);
    }

    public Blog getBlogById(String blogURL, Long id) {
        return WebClient.create(blogURL + "/getBlogById/" + id)
                .get()
                .retrieve()
                .bodyToMono(Blog.class)
                .block();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        id = Long.valueOf(beforeEnterEvent.getRouteParameters().get("id").get());

        Blog blog = blogController.getBlogById(id);

        title.setText(blog.getTitle());
        text.setText(blog.getText());
        date.setText(blog.getDate());
        author.setText(blog.getAuthor().getName());
    }
}
