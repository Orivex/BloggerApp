package com.blacklight.blogger.views;

import com.blacklight.blogger.controllers.BlogController;
import com.blacklight.blogger.entities.Blog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Route("/explore")
public class ExploreBlogsView extends VerticalLayout {

    private final BlogController blogController;

    public ExploreBlogsView(BlogController blogController, @Value("${api.base-url}") String baseURL) {
        this.blogController = blogController;

        String blogURL = baseURL + "/blog";

        setHeight("300px");

        for (Long i = 0L; i < allBlogsSize(blogURL); i++) {

            Mono<Blog> blogMono = getBlogById(blogURL, i+1);

            if(blogMono != null){
                Long id = i+1;
                String title = getBlogTitleById(blogURL, id);
                add(new Button(title, e -> getUI().ifPresent(ui -> ui.navigate(BlogView.class, new RouteParam("id", id)))));
            }
        }
    }

    String getBlogTitleById(String blogURL, Long id) {
        return WebClient.create(blogURL + "/getBlogTitleById/" + id)
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    Mono<Blog> getBlogById(String blogURL, Long id) {
        return WebClient.create(blogURL + "/getBlogById/" + id)
                .get()
                .retrieve()
                .bodyToMono(Blog.class);
    }

    int allBlogsSize(String blogURL) {
        int response = WebClient.create(blogURL + "/size")
                .get()
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return response;
    }
}
