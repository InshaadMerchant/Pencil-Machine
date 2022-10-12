/*
 * Inshaad Merchant 1001861293
 */
package code2_1001861293;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.io.FileNotFoundException;

public class Code2_1001861293
{

    enum ACTION
    {
        DISPENSECHANGE, INSUFFICIENTCHANGE, INSUFFICIENTFUNDS, EXACTPAYMENT,
    }

    public static int PencilMenu()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to my pencil machine");
        System.out.println("Please choose from the following options:");
        System.out.println("0. No pencils for me today");
        System.out.println("1. Purchase pencils");
        System.out.println("2. Check inventory level");
        System.out.println("3. Check change level");
        System.out.printf("Enter your menu choice:");
        int Choice;
        do
        {
            try
            {
                Choice = in.nextInt();
            }
            catch (Exception e)
            {
                Choice = -1;
                in.nextLine();
            }
            if (Choice < 0 || Choice > 3)
            {
                System.out.println("Welcome to my pencil machine");
                System.out.println("Please choose from the following options:");
                System.out.println("0. No pencils for me today");
                System.out.println("1. Purchase pencils");
                System.out.println("2. Check inventory level");
                System.out.println("3. Check change level");
                System.out.println("Invalid menu Choice.");
                System.out.println("Enter your menu choice again:");
            }
        }
        while (Choice < 0 || Choice > 3);

        return Choice;
    }

    public static String displayMoney(int convertamount)
    {
        String dollars = String.valueOf(convertamount / 100);
        String cents = String.valueOf(convertamount % 100);
        int cent = Integer.parseInt(cents);
        if (cent < 10)
        {
            return "$" + dollars + "." + "0" + cents;
        }
        else
        {
            return "$" + dollars + "." + cents;
        }

    }

    public static ACTION buyPencils(int moneyentered, int MyArray[], int quantity, int menu, int total)
    {
        ACTION action = ACTION.EXACTPAYMENT;
        if (total == moneyentered)
        {
            MyArray[1] = MyArray[1] - quantity;
            MyArray[0] = MyArray[0] + moneyentered;
        }
        if (total < moneyentered && MyArray[0] > moneyentered)
        {
            action = ACTION.DISPENSECHANGE;
            MyArray[1] = MyArray[1] - quantity;
            MyArray[0] = MyArray[0] + total;
        }
        if (total < moneyentered && MyArray[0] < moneyentered)
        {
            action = ACTION.INSUFFICIENTCHANGE;
        }
        if (total > moneyentered)
        {
            action = ACTION.INSUFFICIENTFUNDS;
        }
        return action;
    }

    public static void ReadFile(int MyArray[], String File, ArrayList<String> colors)
    {
        File FH = new File(File);
        Scanner inFileRead = null;
        try
        {
            inFileRead = new Scanner(FH);
        }
        catch (Exception e)
        {
            System.out.println("File cannot be opened.");
            System.exit(0);
        }
        String FileLine1 = inFileRead.nextLine();
        String Stringnum[] = FileLine1.split("[ ]");
        MyArray[0] = Integer.parseInt(Stringnum[0]);
        MyArray[1] = Integer.parseInt(Stringnum[1]);
        String FileLine2 = inFileRead.nextLine();
        String MyColorArray[] = FileLine2.split("[ ]");
        for (String it : MyColorArray)
        {
            colors.add(it);
        }
        inFileRead.close();
    }

    public static String PickRandom(ArrayList<String> colors)
    {
        Random rn = new Random();
        int range = colors.size();
        int randomitem = rn.nextInt(range);
        String randomcolor = colors.get(randomitem);
        return randomcolor;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner in = new Scanner(System.in);
        int quantity;
        String amount;
        int MyArray[] = new int[5];
        String filename = args[0].substring(args[0].indexOf('=') + 1);
        String pencilprice = args[1].substring(args[1].indexOf('=') + 1);
        int PENCILPRICE = Integer.parseInt(pencilprice);
        ArrayList<String> colors = new ArrayList<>();
        ReadFile(MyArray, filename, colors);
        int inventorylevel = MyArray[1];
        int change = MyArray[0];
        int Choice = PencilMenu();

        while (Choice != 0)
        {
            switch (Choice)
            {
                case 1:
                    if (MyArray[1] == 0)
                    {
                        System.out.print("The pencil dispenser is out of pencils.\n");
                    }
                    else
                    {
                        amount = displayMoney(PENCILPRICE);
                        System.out.printf("A pencil costs %s\n", amount);
                        System.out.print("How many pencils do you need today?");
                        quantity = in.nextInt();
                        if ((quantity <= 0) || (quantity > MyArray[1]))
                        {
                            System.out.println("Cannot sell that quantity of pencils.");
                        }
                        else
                        {
                            int totalcost = PENCILPRICE * quantity;
                            amount = displayMoney(totalcost);
                            System.out.printf("Your total cost is: %s\n", amount);
                            System.out.print("Enter your money:");
                            int money = in.nextInt();
                            ACTION action = buyPencils(money, MyArray, quantity, Choice, totalcost);
                            switch (action)
                            {
                                case EXACTPAYMENT:
                                    String Randomcolor = PickRandom(colors);
                                    System.out.printf("Here are your %s pencils and Thank you for exact payment\n", Randomcolor);
                                    amount = displayMoney(MyArray[0]);
                                    change = MyArray[0];
                                    inventorylevel = MyArray[1];
                                    System.out.printf("The current inventory level is: %d\n", inventorylevel);
                                    System.out.printf("The amount of change left is %s\n", amount);
                                    break;
                                case DISPENSECHANGE:
                                    Randomcolor = PickRandom(colors);
                                    int changegiven = money - totalcost;
                                    amount = displayMoney(changegiven);
                                    System.out.printf("Here are your %s pencils and your change is %s\n", Randomcolor, amount);
                                    change = MyArray[0];
                                    amount = displayMoney(change);
                                    inventorylevel = MyArray[1];
                                    System.out.printf("The current inventory level is: %d\n", inventorylevel);
                                    System.out.printf("The amount of change left is %s\n", amount);
                                    break;
                                case INSUFFICIENTCHANGE:
                                    System.out.println("not enough change available to complete the purchase");
                                    change = MyArray[0];
                                    amount = displayMoney(MyArray[0]);
                                    inventorylevel = MyArray[1];
                                    System.out.printf("The current inventory level is: %d\n", inventorylevel);
                                    System.out.printf("The amount of change left is %s\n", amount);
                                    break;
                                case INSUFFICIENTFUNDS:
                                    System.out.println("The provided payment was insufficient (no sale took place)");
                                    change = MyArray[0];
                                    amount = displayMoney(MyArray[0]);
                                    inventorylevel = MyArray[1];
                                    System.out.printf("The current inventory level is: %d\n", inventorylevel);
                                    System.out.printf("The amount of change left is %s\n", amount);
                                    break;
                                default:
                                    System.out.printf("Something unknown happened.");
                            }
                        }
                    }
                    break;
                case 2:
                    System.out.printf("The current inventory level is: %d\n", inventorylevel);
                    break;
                case 3:
                    amount = displayMoney(change);
                    System.out.printf("The current change level is: %s\n", amount);
                    break;
                default:
                    System.out.println("Something went wrong");

            }
            Choice = PencilMenu();

        }
        PrintWriter out = new PrintWriter(filename);
        out.printf("%d ", change);
        out.printf("%d\n", inventorylevel);
        for (String it : colors)
        {
            out.printf("%s ", it);
        }
        out.close();

    }

}
