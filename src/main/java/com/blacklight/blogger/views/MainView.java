package com.blacklight.blogger.views;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Mainpage")
@Route("/")
public class MainView extends VerticalLayout {

    public MainView() {
        setHeightFull();
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Button createAuthorAccButton = new Button("Be an author", e -> getUI().ifPresent(ui -> ui.navigate(SignUpAndInAuthorAccountView.class)));
        Button searchForBlog = new Button("Search blogs", e -> getUI().ifPresent(ui -> ui.navigate(BlogSearchView.class)));
        Button exploreBlogsButton = new Button("Explore", e -> getUI().ifPresent(ui -> ui.navigate(ExploreBlogsView.class)));

        exploreBlogsButton.getStyle().setColor("green");
        add(new H1("Welcome to Blogger."), createAuthorAccButton, searchForBlog, exploreBlogsButton);
    }
}