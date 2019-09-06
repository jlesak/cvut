package cz.cvut.fel.omo.blog;

import java.util.List;


/*
* Dashboard serves as a Blog user interface for displaying requested information to user.
 */

public class Dashboard {

    /*
     * Dashboard method for displaying UI context to the user. If the parameter is empty, then method displays warning line. Provided the given parameter
     * is not null, the method iterates the List and delegates printing onto DisplayableComponent itself.
     * @param components  List of Objects implementing DisplayableComponent interface.
     * @param pageHeaders  Header of the dashboard page.
     */
    public void display(List<DisplayableComponent>components, String pageHeader){
        if (components.isEmpty())
            System.out.println("There is no content to displayComponent.");
        else {
            System.out.println("\n" + pageHeader);
            System.out.println("======================================");
            components.forEach(component -> component.displayComponent());
            System.out.println("======================================\n");
        }
    }
}
