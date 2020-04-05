import java.io.*;
import java.util.Properties;

public class Regs {
    public static void Create(Interface inter)
    {
        Properties props = new Properties();
        File regs = new File(inter.naming+".txt");
        try {
            regs.createNewFile();
            props.load(new FileInputStream(regs));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(regs, false);
            fw.write("#Eszdman Tech. Registers from libs. RegNumber:"+ inter.line);
            fw.write(System.lineSeparator());
            fw.write("#"+inter.naming);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeln(fw, "ResWidth"+" = " + inter.WidthRes);
        writeln(fw, "ResHeight"+" = " + inter.HeightRes);
        writeln(fw, "LineL"+" = " + inter.LineL);
        writeln(fw, "FrameL"+" = " + inter.FrameL);
        writeln(fw, "PixelClock"+" = " + inter.pixelclockCap);
        writeln(fw, "Bin1 = "+ inter.bincap1);
        writeln(fw, "Bin2 = " + inter.bincap2);
        writeln(fw, "Fps = "+ inter.FpsCap);
        for(int i = 0; i< inter.line; i++) {
            //try {
                //fw.write(inter.regname[i]+ " = "+ inter.regvalue[i]);
                writeln(fw, inter.regname[i]+ " = "+ inter.regvalue[i]);
                //props.setProperty(inter.regname[i],inter.regvalue[i]);
                //fw.write(System.lineSeparator());
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }

        try {
            //props.store(new FileOutputStream(regs), "Eszdman Tech. Registers from libs.");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void writeln(FileWriter fw, String s){
        try {
            fw.write(s);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //props.setProperty(inter.regname[i],inter.regvalue[i]);

    }
    public static void Read(Interface inter)
    {
        System.out.println("ReadRegs: "+inter.regvalue.length);
        Properties props = new Properties();
        File regs = new File(inter.naming+".txt");
        try {
            props.load(new FileInputStream(regs));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i< inter.line; i++) {
            inter.regvalue[i] = props.getProperty(inter.regname[i]);
        }
        inter.FrameL = Integer.parseInt(props.getProperty("FrameL"));
        inter.LineL = Integer.parseInt(props.getProperty("LineL"));
        inter.WidthRes = Integer.parseInt(props.getProperty("ResWidth"));
        inter.HeightRes = Integer.parseInt(props.getProperty("ResHeight"));
        inter.pixelclockCap = Long.parseLong(props.getProperty("PixelClock"));
        inter.bincap1 = Short.parseShort(props.getProperty("Bin1"));
        inter.bincap2 = Short.parseShort(props.getProperty("Bin2"));
        inter.FpsCap = Double.valueOf(props.getProperty("Fps"));
    }
}
