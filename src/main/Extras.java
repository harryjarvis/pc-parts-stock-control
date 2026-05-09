package main;

public class Extras {
    
    private Extras() {}

    // Reset / Newline
    public static final String reset   = "\033[0m";
    public static final String newline = "\n";

    // Text Styles
    public static final String underline = "\033[4m";
    public static final String bold      = "\033[1m";
    public static final String italic    = "\033[3m";

    // Background Colours
    public static final String cyanBg   = "\033[30;106m";
    public static final String yellowBg = "\033[30;103m";
    public static final String greenBg  = "\033[30;102m";
    public static final String whiteBg  = "\033[30;107m";
    public static final String pinkBg = "\033[0;105m";

    // Colours
    public static final String red    = "\033[91m";
    public static final String orange = "\033[38;2;255;165;0m";
    public static final String yellow = "\033[93m";
    public static final String lime   = "\033[38;2;0;255;0m";
    public static final String cyan   = "\033[96m";
    public static final String purple   = "\033[94m";
    public static final String pink = "\033[95m";
    public static final String gray = "\033[2;3;90m";
    public static final String lightgray = "\033[38;5;248m";
    public static final String white = "\033[97m";
    public static final String black = "\033[30m";
    
    public static final String rbRed = "\033[38;2;218;41;28m";
    public static final String rbYellow = "\033[38;2;255;204;0m";
    public static final String rbNavy = "\033[38;2;47;75;168m";
    public static final String rbWhite = "\033[38;2;255;255;255m";
    
    // Others
    public static final String errorMsg = "\033[3;91m";
    public static final String successMsg = "\033[3;32m";
    public static final String cyanBold = "\033[1;96m";
    public static final String yellowBold = "\033[1;93m";
    public static final String greenBold = "\033[1;92m";
    public static final String HarryPCParts =
    					
            Extras.yellow + "H" +
            Extras.orange + "a" +
            Extras.red 	  + "r" +
            Extras.lime   + "r" +
            Extras.cyan   + "y" +
            Extras.orange + "'" +
            Extras.pink   + "s" +
            Extras.purple + " " +
            Extras.cyan   + "P" +
            Extras.lime   + "C" +
            Extras.yellow + " " +
            Extras.orange + "P" +
            Extras.red    + "a" +
            Extras.orange + "r" +
            Extras.yellow + "t" +
            Extras.lime   + "s" +
            Extras.reset;
    
    public static final String HarryPCPartsBlackBg =
            "\033[40m" + Extras.red    + " H" +
            "\033[40m" + Extras.orange + "a" +
            "\033[40m" + Extras.yellow + "r" +
            "\033[40m" + Extras.lime   + "r" +
            "\033[40m" + Extras.cyan   + "y" +
            "\033[40m" + Extras.purple + "'" +
            "\033[40m" + Extras.pink   + "s" +
            "\033[40m" + Extras.purple + " " +
            "\033[40m" + Extras.cyan   + "P" +
            "\033[40m" + Extras.lime   + "C" +
            "\033[40m" + Extras.yellow + " " +
            "\033[40m" + Extras.orange + "P" +
            "\033[40m" + Extras.red    + "a" +
            "\033[40m" + Extras.orange + "r" +
            "\033[40m" + Extras.yellow + "t" +
            "\033[40m" + Extras.lime   + "s " +
            Extras.reset;
    
   public static void printLogoAnimated() {

        String[] logo = ("""

    """ +
            Extras.red + """
     _    _                       _       _____   _____ 
    """ +
            Extras.white + """
    | |  | |                     ( )     |  __ \\ / ____|
    """ +
            Extras.red + """
    | |__| | __ _ _ __ _ __ _   _|/ ___  | |__) | |     
    """ +
            Extras.white + """
    |  __  |/ _` | '__| '__| | | | / __| |  ___/| |     
    """ +
            Extras.red + """
    | |  | | (_| | |  | |  | |_| | \\__ \\ | |    | |____ 
    """ +
            Extras.white + """
    |_|  |_|\\__,_|_|  |_|   \\__, | |___/ |_|     \\_____|
    """ +
            Extras.red + """
                             __/ |                      
                            |___/                       
    """ +
          
            Extras.white + """
      _____           _       	 ┌────────────────┐
    """ +
            Extras.red + """
     |  __ \\         | |      	 │                │
    """ +
            Extras.white + """
     | |__) |_ _ _ __| |_ ___ 	 │  Remote Stock  │
    """ +
            Extras.red + """
     |  ___/ _` | '__| __/ __|	 │ Control System │
    """ +
            Extras.white + """
     | |  | (_| | |  | |_\\__ \\       │                │
    """ +
            Extras.red + """
     |_|   \\__,_|_|   \\__|___/       └────────────────┘
    """ +
            Extras.reset
        ).split("\n");

        for (String line : logo) {
            System.out.println(line);
            try { Thread.sleep(120); } catch (InterruptedException ignored) {}
        }
    } 
    
}
