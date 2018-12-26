package com.lstar;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    //TODO:
    // const reg nem egyformak es fura nemvet se lehessen adni
    public static void main(String[] args) throws Exception{
        System.out.println("File name: ");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();

        // write your code here
        PicoBlazeCompilator pbc = new PicoBlazeCompilator(name);
        //pbc.printN();
        pbc.printConstantsandNameregs();

        //pbc.printR();
        //pbc.printC();
        InstructionBits b = new InstructionBits("OUTPUT S1 FF");
        System.out.println(b.getInstructionInt());
        //pbc.printIntBits();
        //pbc.printToFile();
        Messages.p("Completed");
    }
}
