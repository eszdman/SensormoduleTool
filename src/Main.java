import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File getDir = new File("");
        System.out.println(getDir.getAbsolutePath());
        //File lib = new File("C:\\Users\\eszdman\\IdeaProjects\\SensormoduleTool\\"+ "com.qti.sensormodule.semco_imx586.bin");
        File lib = new File(args[0]);
        Interface inter = new Interface();
        Configurator.CreateProps(lib.getName());
        inter.dir = lib.getParent();
        inter.name = lib.getName();
        Configurator.GetProps(lib.getName(), inter, false);
        inter.regAstart = inter.regstart+ inter.lineskip*inter.varsize;
        int s = inter.regstart+ inter.lineskip*inter.varsize;
        int k = inter.regsize;
        inter.regname = HexOps.Regs(inter, false);
        System.out.println("num:" + inter.line);
        Configurator.GetProps(lib.getName(), inter, true);
        inter.regstart = s+k-(inter.lineskip*inter.varsize);
        inter.regvalue = HexOps.Regs(inter, true);
        if(!new File(inter.naming+".txt").exists()) {HexOps.GetCaps(inter);Regs.Create(inter);} else {Regs.Read(inter);HexOps.SafeRegs(inter);}
    }
}