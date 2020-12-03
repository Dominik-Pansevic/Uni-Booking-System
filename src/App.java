import javax.swing.*;

public class App {


    public static void main (String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                //initialise the controller
                Controller controller = new Controller();

                //initialise GUIs
                ClerkGUI clerkGUI1 = new ClerkGUI(controller);
                ClerkGUI clerkGUI2 = new ClerkGUI(controller);
                ClerkGUI clerkGUI3 = new ClerkGUI(controller);
                ManagerGUI managerGUI = new ManagerGUI(controller);

                //link GUIs to the controller
                controller.linkGUIs(clerkGUI1,clerkGUI2,clerkGUI3,managerGUI);

                //create threads and add GUIs as a parameter
                Thread thread1 = new Thread(clerkGUI1);
                Thread thread2 = new Thread(clerkGUI2);
                Thread thread3 = new Thread(clerkGUI3);
                Thread thread4 = new Thread(managerGUI);


                //start threads
                thread1.start();
                thread2.start();
                thread3.start();
                thread4.start();


            }
        });
    }


}
