public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equalsIgnoreCase("--cli") || args[0].equalsIgnoreCase("-c") || args[0].equalsIgnoreCase("cli"))) {
            ManagementSystem.main(args);
        } else {
            DisasterManagement.main(args);
        }
    }
}