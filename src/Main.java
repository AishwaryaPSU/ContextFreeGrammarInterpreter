import com.tamuc.cfg.ContextFreeGrammer;
import com.tamuc.cfg.io.HumanInputReader;

// sample input for productions : S -> aSa,S -> bSb, S -> a, S -> b, S -> e
    public class Main {

    public static void main(String[] args) {
        boolean usePreviousGrammer = true;
        HumanInputReader reader = new HumanInputReader();
        ContextFreeGrammer grammer = reader.readCFGFromSTDIN();
        while(true) {
            if(!usePreviousGrammer) {
                grammer = reader.readCFGFromSTDIN();
            }
            System.out.println("The given context Free Grammar is as below");
            System.out.println(grammer.toString());
            System.out.println("Enter the string to be validated");
            String stringTobeValidated = reader.readString();
            if(grammer.validateString(stringTobeValidated)){
                System.out.println("YES. THE STRING IS ACCEPTED BY THE GRAMMAR.");
            } else {
                System.out.println("NO. THE STRING IS NOT ACCEPTED BY THE GRAMMAR.");
            }
            System.out.println("Do you want to use the same grammar test more strings [y/n]");
            String useSame = reader.readString();
            switch (useSame.toLowerCase()){
                case "n":
                    usePreviousGrammer = false;
                    break;
                default:
                    usePreviousGrammer =  true;
            }
        }

    }
}
