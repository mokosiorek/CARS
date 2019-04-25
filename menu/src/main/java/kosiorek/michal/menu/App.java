package kosiorek.michal.menu;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        var filename = "testcars.json";
        var menuService = new MenuService(filename);
        menuService.mainMenu();
    }

}
