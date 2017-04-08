
public class Main {

    public static void main(String[] args) {
	Arena arena = new Arena();
	Navigator navigator = new Navigator();
	ActionManager manager = new ActionManager(arena, navigator);
	manager.navigate();

	System.out.println("Complete");

	arena.printMap(navigator.getHeight(), navigator.getWidth());

	/*
	 * TRIGGER START HERE
	 * 
	 * 
	 */
    }

}
